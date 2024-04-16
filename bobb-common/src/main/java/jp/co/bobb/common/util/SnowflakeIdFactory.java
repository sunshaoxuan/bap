package jp.co.bobb.common.util;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2019/01/11
 */
public class SnowflakeIdFactory {
    /**
     * shift for smaller timestamp
     */
    private final static long EPOCH = 0L;
    private final static long DEVICE_ID_BITS = 2L;
    private final static long SEQUENCE_BITS = 16L;
    /**
     * 与& 非~ 或| 异或^, only the bit on WORKER_ID_BITS are 1
     */
    private final static long MAX_WORKER_ID = -1L ^ -1L << DEVICE_ID_BITS;
    private final static int SEQUENCE_MASK = (int) (-1L ^ -1L << SEQUENCE_BITS);

    private final long deviceId;
    private final RecyclableAtomicInteger atomic = new RecyclableAtomicInteger();
    private long lastTimestamp = -1L;

    public SnowflakeIdFactory(final long deviceId) {
        if (deviceId > MAX_WORKER_ID || deviceId < 0) {
            throw new IllegalArgumentException(
                    String.format("Device ID should be between 0 and %d", MAX_WORKER_ID));
        }
        this.deviceId = deviceId;
    }

    public static void main(String[] args) {
        SnowflakeIdFactory worker = new SnowflakeIdFactory(1);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            System.out.println(worker.nextId());
            //worker.nextId();
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Total: " + duration + "ms, " + 100 / duration + "/ms");
    }

    public long nextId() {
        long timestamp = millisecond();
        if (timestamp < lastTimestamp) {
            throw new IllegalArgumentException(
                    String.format("Wait %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            int sequence = atomic.incrementAndRecycle(SEQUENCE_MASK);
            if (sequence == 0) {
                timestamp = waitTilNextMillis(lastTimestamp);
                lastTimestamp = timestamp;
            }
            return (timestamp - EPOCH << (SEQUENCE_BITS + DEVICE_ID_BITS)) | (deviceId << SEQUENCE_BITS) | sequence;
        } else {
            atomic.set(0);
            lastTimestamp = timestamp;
            return (timestamp - EPOCH << (SEQUENCE_BITS + DEVICE_ID_BITS)) | (deviceId << SEQUENCE_BITS);
        }
    }

    private long waitTilNextMillis(final long lastTimestamp) {
        System.out.print(lastTimestamp);
        long timestamp;
        for (; ; ) {
            timestamp = this.millisecond();
            System.out.print('+');
            if (timestamp > lastTimestamp) {
                System.out.print("\n");
                return timestamp;
            }
        }
    }

    private long millisecond() {
        return System.currentTimeMillis();
    }
}
