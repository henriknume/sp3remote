package io.hnnt.sp3remote.events;

/**
 * Created by nume on 2016-05-11.
 */
public class ControlEvent {

    public final String fullMessage;
    public final int performedAction;

    public static final int PANEL_MOVING_UP = 1;
    public static final int PANEL_MOVING_LEFT = 2;
    public static final int PANEL_MOVING_RIGHT = 3;
    public static final int PANEL_MOVING_DOWN = 4;
    public static final int PANEL_STOPPED = 5;
    public static final int PANEL_RUNNING_AUTO = 6;
    public static final int PARSE_ERROR = -1;

    public ControlEvent(String message) {
        this.fullMessage = message;
        performedAction = parsePerformedAction(message);
;
    }

    private int parsePerformedAction(String message) {
        String trimmedMessage = message.replaceAll("(\\r|\\n|\\s)", "");
        int action = PARSE_ERROR;

        if(trimmedMessage.contains("runu")) {
            action = PANEL_MOVING_UP;
        }

        else if(trimmedMessage.contains("runl")) {
            action = PANEL_MOVING_LEFT;
        }

        else if(trimmedMessage.contains("runr")) {
            action = PANEL_MOVING_RIGHT;
        }

        else if(trimmedMessage.contains("rund")) {
            action = PANEL_MOVING_DOWN;
        }

        else if(trimmedMessage.contains("runstop")) {
            action = PANEL_STOPPED;
        }

        else if(trimmedMessage.contains("runauto")) {
            action = PANEL_RUNNING_AUTO;
        }

        return action;
    }
}