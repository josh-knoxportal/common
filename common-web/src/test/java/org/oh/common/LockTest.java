package org.oh.common;

import java.util.concurrent.locks.ReentrantLock;

public class LockTest {
	protected ReentrantLock lock = new ReentrantLock();
	protected Integer n = 1;
	protected Integer count = 0;

	public int getCount() throws Exception {
//		lock.lock();
//		try {
		synchronized (count) {
		System.out.println(n++ + " " + Thread.currentThread().getName() + " count: " + count);
		Thread.sleep(4000);
		System.out.println(n++ + " " + Thread.currentThread().getName() + " count: " + count);
		return count;
//		} finally {
//			lock.unlock();
		}
	}

	public void addCount() throws Exception {
//		lock.lock();
//		try {
		synchronized (count) {
		count++;
//		} finally {
//			lock.unlock();
		}
	}

	public void resetCount() throws Exception {
//		lock.lock();
//		try {
//		synchronized (count) {
		count = 0;
//		} finally {
//			lock.unlock();
//		}
	}

	public static void main(String[] args) {
		final LockTest object = new LockTest();

		Thread t1 = new Thread("FIRST_THREAD") {
			@Override
			public void run() {
				try {
//					while (object.getCount() < 10) {
					object.getCount();
					System.out.println(Thread.currentThread().getName() + " count: " + object.count);

					object.resetCount();
//					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};

		Thread t2 = new Thread("SECOND_THREAD") {
			@Override
			public void run() {
				try {
//					while (object.getCount() < 10) {
					Thread.sleep(2000);

					object.addCount();
//					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};

		t1.start();
		t2.start();
	}
}