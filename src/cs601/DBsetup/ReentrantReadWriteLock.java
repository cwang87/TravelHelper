package cs601.DBsetup;

import java.util.HashMap;

/**
 * A reentrant read/write lock that allows: 1) Multiple readers (when there is
 * no writer). 2) One writer (when nobody else is writing or reading). 3) A
 * writer is allowed to acquire a read lock while holding the write lock. The
 * assignment is based on the assignment of Prof. Rollins (original author).
 */
public class ReentrantReadWriteLock {

	// TODO: Add instance variables : you need to keep track of the read lock
	// holders and the write lock holders.
	// We should be able to find the number of read locks and the number of
	// write locks
	// a thread with the given threadId is holding
	private volatile int readLockCount;
	private volatile int writeLockCount;
	private final HashMap<Long, Integer> readersMap;
	private final HashMap<Long, Integer> writersMap;

	/**
	 * Constructor for ReentrantReadWriteLock
	 */
	public ReentrantReadWriteLock() {
		readLockCount = 0;
		writeLockCount = 0;
		readersMap = new HashMap<>();
		writersMap = new HashMap<>();
	}

	/**
	 * @return - Returns true if the current thread holds a read lock.
	 */
	public synchronized boolean isReadLockHeldByCurrentThread() {
		if (readLockCount > 0 && readersMap.containsKey(Thread.currentThread().getId()))
			return true;
		else
			return false;
	}

	/**
	 * @return - Returns true if the current thread holds a write lock.
	 */
	public synchronized boolean isWriteLockHeldByCurrentThread() {
		if (writeLockCount > 0 && writersMap.containsKey(Thread.currentThread().getId()))
			return true;
		else
			return false;
	}

	/**
	 * Non-blocking method that tries to acquire the read lock. Returns true if
	 * successful.
	 * 
	 * @return true - if it succeeds to acquire the read lock false - if it
	 *         fails to acquire the read lock
	 */
	public synchronized boolean tryAcquiringReadLock() {
		
		long currentThreadID = Thread.currentThread().getId();
		
		/*
		 * If there is no writer, any thread is eligible to get read locks.
		 */
		if (writeLockCount == 0 && !isReadLockHeldByCurrentThread()) {
			readLockCount++;
			readersMap.put(currentThreadID, 1);
			return true;
		}else if(writeLockCount == 0 && isReadLockHeldByCurrentThread()){
			readLockCount++;
			readersMap.put(currentThreadID, readersMap.get(currentThreadID)+1);
			return true;
		/*
		 * If current thread is holding the write lock (probably more than one), it's also eligible to hold read locks.
		 */
		} else if (writeLockCount > 0 && isWriteLockHeldByCurrentThread() && !isReadLockHeldByCurrentThread()) {
			readLockCount++;
			readersMap.put(currentThreadID, 1);
			return true;
		} else if (writeLockCount > 0 && isWriteLockHeldByCurrentThread() && isReadLockHeldByCurrentThread()) {
			readLockCount++;
			readersMap.put(currentThreadID, readersMap.get(currentThreadID) + 1);
			return true;
		} else
			return false;
	}

		long currentThreadID = Thread.currentThread().getId();


	/**
	 * Non-blocking method that tries to acquire the write lock. Returns true if
	 * successful.
	 * 
	 * @return true - if it succeeds to acquire the write lock false - if it
	 *         fails to acquire the write lock
	 */
	public synchronized boolean tryAcquiringWriteLock() {

		long currentThreadID = Thread.currentThread().getId();

		/*
		 * When no reader or writer exist, any thread is eligible to be the
		 * writer. And, this is the first time for the thread to be added to the
		 * writersMap.
		 */
		if (writeLockCount == 0 && readLockCount == 0) {
			writeLockCount++;
			writersMap.put(currentThreadID, 1);
			return true;
		}

		/*
		 * If the current thread is the writer, it can get another write lock.
		 * Meanwhile, we need to increase its value by one in the writersMap.
		 */

		else if (writeLockCount > 0 && isWriteLockHeldByCurrentThread()) {
			writeLockCount++;
			writersMap.put(currentThreadID, writersMap.get(currentThreadID) + 1);
			return true;
		} else
			return false;
	}

	/**
	 * Blocking method - calls tryAcquiringReadLock and returns only when the
	 * read lock has been acquired, otherwise waits.
	 * 
	 */
	public synchronized void lockRead() {
		if (!tryAcquiringReadLock()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Releases the read lock held by the current thread.
	 */
	public synchronized void unlockRead() {

		long currentThreadID = Thread.currentThread().getId();

		readLockCount--;
		readersMap.put(currentThreadID, readersMap.get(currentThreadID) - 1);
		if (readersMap.get(currentThreadID) == 0)
			readersMap.remove(currentThreadID);

		/*
		 * if readLockCount becomes 0, all threads waiting to write will be
		 * notified and acquire the write lock.
		 */

		if (readLockCount == 0)
			notifyAll();
	}

	/**
	 * Blocking method that calls tryAcquiringWriteLock and returns only when
	 * the write lock has been acquired, otherwise waits.
	 * 
	 */
	public synchronized void lockWrite() {
		while (!tryAcquiringWriteLock()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Releases the write lock held by the current thread.
	 */

	public synchronized void unlockWrite() {

		long currentThreadID = Thread.currentThread().getId();

		writeLockCount--;
		writersMap.put(currentThreadID, writersMap.get(currentThreadID) - 1);

		if (writersMap.get(currentThreadID) == 0)
			writersMap.remove(currentThreadID);

		notifyAll();
	}
}
