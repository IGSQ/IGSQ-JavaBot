package org.igsq.igsqbot.objects;

/*
 *     Copyright 2020 Horstexplorer @ https://www.netbeacon.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import net.dv8tion.jda.api.events.GenericEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Waits for desired events
 */
public class EventWaiter 
{
    private static final ConcurrentHashMap<Package<?>, Integer> eventList = new ConcurrentHashMap<>();

    /**
     * Waits for a specific event to happen, returns the event when detected
     *
     * @param eventClassToWait wait for events of this class
     * @param condition additional check to differentiate from other events of the same class
     * @param timeout after which to cancel the wait
     * @param <T> class of the event
     * @return event of success, null on timeout
     */
    public <T extends GenericEvent> T waitFor(Class<T> eventClassToWait, Predicate<T> condition, long timeout) throws InterruptedException
    {
        Package<T> tPackage = new Package<>(eventClassToWait, condition);
        eventList.put(tPackage, 0);
        long l = System.currentTimeMillis();
        synchronized (tPackage)
        { 
        	tPackage.wait(timeout); 
        }
        eventList.remove(tPackage);
        if(l+timeout-1 < System.currentTimeMillis()) // the 1ms diff makes it a lot easier to detect a timeout (but also a bit more likely to get it wrong)
        { 
            return null;
        }
        return tPackage.getEvent();
    }

    /**
     * Used to test if something is waiting on this event to happen
     *
     * @param event the current event
     * @param <T> the event class
     * @return true if something has been waiting for this event
     */
    public static <T extends GenericEvent> boolean waitingOnThis(T event)
    {
        for(Package<?> p : eventList.keySet())
        {
            if(p.tryFinish(event))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Used for internal processing
     *
     * @param <E>
     */
    public static class Package<E extends GenericEvent>
    {
        private final Class<E> classToWait;
        private final Predicate<E> condition;
        private E event;

        /**
         * Creates a new object of this class
         *
         * @param classToWait wait for objects of this class
         * @param condition to check whether the object is the desired one
         */
        protected Package(Class<E> classToWait, Predicate<E> condition)
        {
            this.classToWait = classToWait;
            this.condition = condition;
        }

        /**
         * Tries to serve the event to this object
         *
         * @param event the event
         * @return true on success
         */
        @SuppressWarnings({"unchecked"})
        public boolean tryFinish(GenericEvent event)
        {
            if(classToWait == event.getClass())
            {
                if(condition.test((E) event))
                {
                    this.event = (E) event;
                    synchronized(this)
                    {
                    	this.notifyAll();
                    }
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns the event
         *
         * @return event
         */
        public E getEvent(){
            return event;
        }
    }
}

