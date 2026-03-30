package helpers;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ActionHelper {
    public static AbstractAction create( Runnable action ) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        };
    }
}
