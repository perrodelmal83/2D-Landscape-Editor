package org.openrsc.editor.event;

import com.google.common.eventbus.EventBus;

public class EventBusFactory {
    private static EventBus EVENT_BUS = new EventBus();

    public static EventBus getEventBus() {
        return EVENT_BUS;
    }
}
