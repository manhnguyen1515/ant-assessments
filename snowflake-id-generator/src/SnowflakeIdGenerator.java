public class SnowflakeIdGenerator {
    private final long epoch = 1700000000000L; // Custom epoch (e.g., Nov 2023)

    private final long nodeIdBits = 10L;
    private final long sequenceBits = 12L;

    private final long maxNodeId = (1L << nodeIdBits) - 1;
    private final long maxSequence = (1L << sequenceBits) - 1;

    private final long nodeIdShift = sequenceBits;
    private final long timestampShift = sequenceBits + nodeIdBits;

    private final long nodeId;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowflakeIdGenerator(long nodeId) {
        if (nodeId < 0 || nodeId > maxNodeId) {
            throw new IllegalArgumentException("Node ID must be between 0 and " + maxNodeId);
        }
        this.nodeId = nodeId;
    }

    public synchronized long nextId() {
        long currentTimestamp = currentTime();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                // Sequence exhausted for this millisecond, wait for next millisecond
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - epoch) << timestampShift)
                | (nodeId << nodeIdShift)
                | sequence;
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp <= lastTimestamp) {
            currentTimestamp = currentTime();
        }
        return currentTimestamp;
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }
}
