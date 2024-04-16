package jp.co.bobb.common.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/01/11
 */
public class RecyclableAtomicInteger extends AtomicInteger {
    /**
     * Atomically increments by one the current value, or return
     * to zero if the value exceeds threshold
     *
     * @return the updated value
     */
    public final int incrementAndRecycle(int threshold) {
        for (; ; ) {
            int current = get();
            int next = (current + 1) % threshold;
            if (compareAndSet(current, next)) {
                return next;
            }
        }
    }
}
