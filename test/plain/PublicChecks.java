import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Public examples of required behaviour. Add independent tests of your own. */
public final class PublicChecks {
    @FunctionalInterface public interface Action { void run() throws Exception; }
    public record Case(String name, Action action) {}
    public static void equal(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + "; got " + actual);
        }
    }
    public static void require(boolean condition, String message) { 
        if (!condition) {
            throw new AssertionError(message); 
        }
    }
    public static void rejects(Class<? extends Throwable> type, Action action) throws Exception {
        try { action.run(); } catch (Throwable ex) {
            if (type.isInstance(ex)) return;
            throw new AssertionError("Expected " + type.getSimpleName() + "; got " + ex, ex);
        }
        throw new AssertionError("Expected " + type.getSimpleName() + "; operation returned normally");
    }
    public static List<Case> cases() {
        List<Case> cases = new ArrayList<>();

    cases.add(new Case("injected notifier called once", () -> { 
        RecordingNotifier n = new RecordingNotifier(); 
        new Service(n).confirm("B1"); 
        equal(java.util.List.of("B1"),n.snapshot()); 
    }));

    cases.add(new Case("null collaborator", () -> 
        rejects(IllegalArgumentException.class, () -> 
        new Service(null))));

    cases.add(new Case("blank rejected without send", () -> { 
        RecordingNotifier n=new RecordingNotifier(); 
        Service s=new Service(n); 
        rejects(IllegalArgumentException.class, () -> s.confirm("  ")); 
        equal(0,n.snapshot().size()); 
    }));

    cases.add(new Case("null ID rejected without send", () -> { 
        RecordingNotifier n=new RecordingNotifier(); 
        Service s=new Service(n); 
        rejects(IllegalArgumentException.class, () -> s.confirm(null)); 
        equal(0,n.snapshot().size()); 
    }));

    cases.add(new Case("immutable detached snapshot", () -> { 
        RecordingNotifier n=new RecordingNotifier(); 
        n.send("B1"); var old=n.snapshot(); 
        rejects(UnsupportedOperationException.class, () -> old.add("B2")); 
        n.send("B3"); 
        equal(java.util.List.of("B1"),old); 
        equal(java.util.List.of("B1","B3"),n.snapshot()); 
    }));

    cases.add(new Case("independent services", () -> { 
        RecordingNotifier a=new RecordingNotifier(),b=new RecordingNotifier(); 
        new Service(a).confirm("B1"); 
        equal(java.util.List.of("B1"),a.snapshot()); 
        equal(0,b.snapshot().size()); 
    }));

        return cases;
    }
    public static void main(String[] args) {
        int failed = 0;
        for (Case c : cases()) {
            try { c.action().run(); System.out.println("PASS " + c.name()); }
            catch (Throwable ex) { failed++; System.out.println("FAIL " + c.name() + ": " + ex); }
        }
        System.out.println("CHECKS " + cases().size() + "; FAILED " + failed);
        if (failed != 0) System.exit(1);
    }
}
