package server.commands;

import connect_utils.CommandInfo;
import server.Logger;
import server.connection_control.User;

import java.io.IOException;
import java.util.logging.Level;

public class ExitCommand extends Command {
    ExitCommand() {
        super("exit", "", "завершает выполнение программы", CommandInfo.SendInfo.EXIT,
                null, false);
    }

    /**
     * exit from command
     *
     * @param programController that uses for program
     * @param args              for command from console input (args[0] is program name)
     */
    @Override
    public String execute(User user, ProgramController programController, String[] args) throws IOException {
        user.disconnect();
        Logger.getLogger().log(Level.INFO, "Пользователь " +
                (user.getLogin() == null ? user.getAddress() : user.getLogin()) + " отключился от сервера.");
        return null;
    }
}