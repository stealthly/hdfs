package org.apache.mesos.hdfs.scheduler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mesos.hdfs.config.ConfigServer;

/**
 * Main entry point for the Scheduler.
 */
public final class Main {

  private final Log log = LogFactory.getLog(Main.class);

  public static void main(String[] args) {
    new Main().start();
  }

  private void start() {
    Injector injector = Guice.createInjector();
    getSchedulerThread(injector).start();
    injector.getInstance(ConfigServer.class);
  }

  private Thread getSchedulerThread(Injector injector) {
    Thread scheduler = new Thread(injector.getInstance(HdfsScheduler.class));
    scheduler.setName("HdfsScheduler");
    scheduler.setUncaughtExceptionHandler(getUncaughtExceptionHandler());
    return scheduler;
  }

  private Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {

    return new Thread.UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread t, Throwable e) {
        log.error("Scheduler exiting due to uncaught exception", e);
        System.exit(2);
      }
    };
  }
}
