package cn.dreamrealm.emulator;

public class Memory {

    int[] memory = new int[256];

    public int load(int address) {
        if (address < 0 || address >= memory.length) {
            throw new RuntimeException("�ڴ���ʴ��󣬵�ַ��" + address);
        }
        return memory[address];
    }

    public void store(int address, int value) {
        if (address < 0 || address >= memory.length) {
            throw new RuntimeException("�ڴ���ʴ��󣬵�ַ��" + address);
        }
        memory[address] = value;
    }

    public void reset() {
        for (int i = 0; i < memory.length; i++) {
            memory[i] = 0;
        }
    }
}
