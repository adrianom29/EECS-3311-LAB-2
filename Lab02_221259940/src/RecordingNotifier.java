import java.util.ArrayList;
    import java.util.List;
    public final class RecordingNotifier implements Notifier {
        private final List<String> messages;

        public RecordingNotifier(){
            this.messages = new ArrayList<>();
        }

        public void send(String bookingId) { 
            messages.add(bookingId); 
        }

        public List<String> snapshot() {
    // TODO: return an immutable detached snapshot, not the mutable list.
            return List.copyOf(messages);
        }
    }