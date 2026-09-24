public final class ConsoleNotifier implements Notifier {
        public void send(String bookingId) {
            System.out.println("SIMULATED notification: " + bookingId); 
        }
    }