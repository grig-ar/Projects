package searchers;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class FileSearcher {

    private static final File POISON_PILL = new File("");

    private final String filename;
    private final File baseDir;
    private final int concurrency;
    private final AtomicLong count;

    private static class RunnableDirSearch implements Runnable {
        private final BlockingQueue<File> dirQueue;
        private final BlockingQueue<File> fileQueue;
        private final AtomicLong count;
        private final int num;

        RunnableDirSearch(final BlockingQueue<File> dirQueue, final BlockingQueue<File> fileQueue, final AtomicLong count, final int num) {
            this.dirQueue = dirQueue;
            this.fileQueue = fileQueue;
            this.count = count;
            this.num = num;
        }

        @Override
        public void run() {
            try {
                File dir = dirQueue.take();
                while (dir != POISON_PILL) {
                    final File[] fi = dir.listFiles();
                    if (fi != null) {
                        int len = fi.length;
                        for (int i = 0; i < len; ++i) {
                            if (fi[i].isDirectory()) {
                                count.incrementAndGet();
                                dirQueue.put(fi[i]);
                            } else {
                                fileQueue.put(fi[i]);
                            }
                        }
                    }
                    final long c = count.decrementAndGet();
                    if (c == 0) {
                        end();
                    }
                    dir = dirQueue.take();
                }
            } catch (final InterruptedException ie) {

            }
        }

        private void end() {
            try {
                fileQueue.put(POISON_PILL);
            } catch (final InterruptedException e) {

            }
            for (int i = 0; i < num; i++) {
                try {
                    dirQueue.put(POISON_PILL);
                } catch (final InterruptedException e) {
					
                }
            }
        }
    }

    private static class CallableFileSearch implements Callable<ArrayList<File>> {
        private final BlockingQueue<File> fileQueue;
        private final ArrayList<File> foundFiles = new ArrayList<>();
        private final String name;

        CallableFileSearch(final BlockingQueue<File> fileQueue, final String name) {
            this.fileQueue = fileQueue;
            this.name = name;
        }

        @Override
        public ArrayList<File> call() throws Exception {
            File file = fileQueue.take();
            while (file != POISON_PILL) {
                final String filename = file.getName().toLowerCase();
                final String lf = name.toLowerCase();
                if (filename.equalsIgnoreCase(name) || filename.startsWith(lf) || filename.endsWith(lf)) {
                    foundFiles.add(file);
                }
                file = fileQueue.take();
            }
            return foundFiles;
        }
    }


    public FileSearcher(final String filename, final File baseDir, final int concurrency) {
        this.filename = filename;
        this.baseDir = baseDir;
        this.concurrency = concurrency;
        count = new AtomicLong(0);
    }

    public ArrayList<File> find() {
        final ExecutorService ex = Executors.newFixedThreadPool(concurrency + 1);
        final BlockingQueue<File> dirQueue = new LinkedBlockingQueue<>();
        final BlockingQueue<File> fileQueue = new LinkedBlockingQueue<>(10000);
        for (int i = 0; i < concurrency; i++) {
            ex.submit(new RunnableDirSearch(dirQueue, fileQueue, count, concurrency));
        }
        count.incrementAndGet();
        dirQueue.add(baseDir);
        final Future<ArrayList<File>> c = ex.submit(new CallableFileSearch(fileQueue, filename));
        try {
            return c.get();
        } catch (final Exception e) {
            return null;
        } finally {
            ex.shutdownNow();
        }
    }
}