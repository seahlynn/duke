import java.util.ArrayList;

/**
 * Deals with making sense of user input.
 */
public class Parser {

    /**
     * Creates a Parser object.
     */
    public Parser() {
    }

    /**
     * Returns differnt types of Command objects depending on user input.
     * @param command represents the input by the user
     * @return Command object according to what the user inputs
     * @throws DukeException when the input is missing information or
     *                       in the wrong format
     */

    public static Command parse(String command) throws DukeException {
        String[] commandSplit = command.split(" ");
        String type = commandSplit[0];
        Type enumType;
        String[] timeSplit;

        switch (type) {
        case "list":
            return new ListCommand();

        case "bye":
            return new ExitCommand();
        }

        if (commandSplit.length == 1) {
            throw throwMissingDescException(type);
        }

        switch (type) {
        case "done":
            return new DoneCommand(commandSplit[1]);

        case "delete":
            return new DeleteCommand(commandSplit[1]);

        case "find":
            String[] wordsArray = keywordsToArray(commandSplit);
            return new KeyCommand(wordsArray);

        case "todo":
            enumType = Type.valueOf(type.toUpperCase());
            assert enumType == Type.TODO : "Enum is not right!";
            return new AddCommand(enumType, command.substring(5), "");

        case "deadline":
            enumType = Type.valueOf(type.toUpperCase());
            assert enumType == Type.DEADLINE : "Enum is not right!";
            timeSplit = splitCommandForTask(type, command);
            return new AddCommand(enumType, timeSplit[0].substring(9), timeSplit[1]);

        case "event":
            enumType = Type.valueOf(type.toUpperCase());
            assert enumType == Type.EVENT : "Enum is not right!";
            timeSplit = splitCommandForTask(type, command);
            return new AddCommand(enumType, timeSplit[0].substring(6), timeSplit[1]);

        default:
            assert false : "Program not supposed to reach this line!";
            return null;
        }
    }

    private static String[] keywordsToArray(String[] keywords) {
        ArrayList<String> findWords = new ArrayList<>();
        for (int i = 1; i < keywords.length; i++) {
            findWords.add(keywords[i]);
        }
        String[] wordsArray = new String[findWords.size()];
        return findWords.toArray(wordsArray);
    }

    private static String[] splitCommandForTask(String type, String command) throws DukeException {
        String[] timeSplit;

        if (type.equals("event")) {
            timeSplit = command.split("/at");
        } else {
            timeSplit = command.split("/by");
        }

        if (timeSplit.length == 1) {
            throw new DukeException("☹ OOPS!!! Please enter a time for your task.");
        }

        return timeSplit;
    }

    private static DukeException throwMissingDescException(String type) {
        DukeException toThrow = null;

        switch (type) {
        case "done":
            toThrow = new DukeException("☹ OOPS!!! Please indicate " +
                    "which task you have completed.");
            break;
        case "delete":
            toThrow = new DukeException("☹ OOPS!!! Please indicate which task "
                    + "you would like to delete.");
            break;
        case "find":
            toThrow = new DukeException("☹ OOPS!!! Please indicate the keyword "
                    + "you are looking for!");
            break;
        case "todo":
        case "deadline":
        case "event":
            toThrow = new DukeException("☹ OOPS!!! The description of a "
                    + "task cannot be empty.");
            break;
        default:
             toThrow = new DukeException(" ☹ OOPS!!! I'm sorry, but I don't "
                     + "know what that means :-(");
        }

        return toThrow;
    }
}
