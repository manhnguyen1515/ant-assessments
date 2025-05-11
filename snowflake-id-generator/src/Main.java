//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1); // nodeId = 1
        for (int i = 0; i < 5; i++) {
            long id = generator.nextId();
            System.out.println("Generated ID: " + id);
        }
    }
}
