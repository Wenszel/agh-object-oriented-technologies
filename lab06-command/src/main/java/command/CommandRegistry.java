package command;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandRegistry {

	private ObservableList<Command> commandStack = FXCollections
			.observableArrayList();
	private ObservableList<Command> redoStack = FXCollections
			.observableArrayList();

	public void executeCommand(Command command) {
		command.execute();
		commandStack.add(command);
		redoStack.clear();
	}

	public void redo() {
		if (!redoStack.isEmpty()) {
			Command command = redoStack.get(redoStack.size() - 1);
			command.redo();
			redoStack.remove(command);
			commandStack.add(command);
		}
	}

	public void undo() {
		if (!commandStack.isEmpty()) {
			Command command = commandStack.get(commandStack.size() - 1);
			command.undo();
			commandStack.remove(command);
			redoStack.add(command);
		}
	}

	public ObservableList<Command> getCommandStack() {
		return commandStack;
	}
}
