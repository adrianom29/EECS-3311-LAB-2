public final class Service {
        private final Notifier notifier;

        public Service(Notifier notifier) throws IllegalArgumentException{
            // TODO: reject null and retain the supplied collaborator.
            if (notifier == null) {
                throw new IllegalArgumentException();
            }
            this.notifier = notifier;
        }

        public void confirm(String bookingId) throws IllegalArgumentException {
            // TODO: reject null/blank IDs before invoking the supplied notifier exactly once.
            if (bookingId == null || bookingId.isBlank()){
                throw new IllegalArgumentException();
            }
            notifier.send(bookingId);
        }
    }