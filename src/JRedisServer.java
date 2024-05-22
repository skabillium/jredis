import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import commands.*;
import database.Database;
import errors.*;
import resp.RespDeserializer;
import resp.RespSerializer;
import resp.RespSimpleString;

public class JRedisServer {
    private CliOptions config;
    private FileOutputStream walStream;

    public JRedisServer(CliOptions config) {
        this.config = config;
    }

    public void start() throws FileNotFoundException {
        if (config.walEnabled) {
            var wal = new File(config.wal);
            var parentPath = wal.getParent();
            if (parentPath != null) {
                var dir = new File(wal.getParent());
                var created = dir.mkdirs();
                if (!created) {
                    System.out.println("Could not create parent dir");
                    return;
                }
            }
            walStream = new FileOutputStream(config.wal);
        }

        try {
            // Create a server socket that listens on port 12345
            ServerSocket serverSocket = new ServerSocket(config.port);
            System.out.printf("JRedis server started at port %d \n", this.config.port);

            // Accept client connections
            Socket clientSocket = serverSocket.accept();

            var inputStream = clientSocket.getInputStream();
            var context = new ConnectionContext(inputStream, clientSocket.getOutputStream());
            var db = new Database();
            var deserializer = new RespDeserializer(context.getInputStream());
            while (true) {
                try {
                    var payload = deserializer.deserialize();
                    var message = switch (payload) {
                        case String s -> s;
                        case List<?> l -> {
                            var strings = new ArrayList<String>();
                            for (var s : l) {
                                strings.add(s.toString());
                            }
                            yield String.join(" ", strings);
                        }
                        case null -> null;
                        default -> throw new IOException("Type not supported");
                    };

                    if (message == null) {
                        break;
                    }

                    message = message.trim();
                    var command = CommandParser.parseCommand(message);

                    if (config.walEnabled && command.isUpdate) {
                        walAppend(message);
                    }

                    var result = executeCommand(db, command);
                    switch (result) {
                        case RespSimpleString simple -> context.simple(simple.toString());
                        case String str -> context.write(str);
                        case ArrayList<?> col -> context.write((ArrayList<String>) col);
                        case Integer i -> context.write(i);
                        default -> context.nil();
                    }
                } catch (NotFoundException e) {
                    context.nil();
                } catch (IOException e) {
                    context.write(e);
                    break;
                } catch (Exception e) {
                    context.write(e);
                }
            }

            // Close the client socket
            clientSocket.close();
            // Close the server socket
            serverSocket.close();

            if (config.walEnabled) {
                walStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object executeCommand(Database db, Command command) throws WrongTypeException, NotFoundException {
        return switch (command) {
            case Command.InfoCommand info -> new RespSimpleString("JRedis server version 0.0.0");
            case Command.PingCommand ping -> new RespSimpleString("PONG");
            case Command.KeysCommand keys -> db.listKeys(keys.pattern);
            case Command.SetCommand set -> {
                db.set(set.key, set.value, set.expires);
                yield new RespSimpleString("OK");
            }
            case Command.GetCommand get -> db.get(get.key);
            case Command.DeleteCommand del -> db.delete(del.keys);
            case Command.LLenCommand llen -> db.listLength(llen.key);
            case Command.LPushCommand lpush -> db.listLPush(lpush.key, lpush.values);
            case Command.RPopCommand rpop -> db.listRPop(rpop.key);
            case Command.LPopCommand lpop -> db.listLPop(lpop.key);
            default -> null;
        };
    }

    private void walAppend(String message) throws IOException {
        walStream.write(RespSerializer.serialize(message).getBytes());
    }
}
