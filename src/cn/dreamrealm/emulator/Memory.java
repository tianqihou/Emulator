package cn.dreamrealm.emulator;

public class Memory {

    int[] data = new int[256];

    int lastAccess = -1;

    public int load(int address) {
        if (address < 0 || address >= data.length) {
            throw new RuntimeException("内存访问错误，地址：" + address);
        }
        lastAccess = address;
        return data[address];
    }

    public void store(int address, int value) {
        if (address < 0 || address >= data.length) {
            throw new RuntimeException("内存访问错误，地址：" + address);
        }
        lastAccess = address;
        data[address] = value;
    }

    public void reset() {
        lastAccess = -1;
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }
    }
}
