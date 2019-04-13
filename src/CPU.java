package cn.dreamrealm.emulator;

public class CPU {

    Memory memory;

    int[] gpr;

    int ip;

    int sp;

    boolean zero;

    boolean carry;

    boolean fault;

    void init() {
        memory = new Memory();
        gpr = new int[] { 0, 0, 0, 0 };
        ip = 0;
        sp = 0;
        zero = false;
        carry = false;
        fault = false;
    }

    void step() {
        if (fault) {
            throw new RuntimeException("´íÎó£¬ÖØÖÃcpu¼ÌÐø");
        }
        int instr = memory.load(ip);
        switch (instr) {
        case Opcodes.ADD_REG_TO_REG:
            int regTo = memory.load(ip + 1);
            int regFrom = memory.load(ip + 2);
            int value = processResult(readRegister(regTo) + readRegister(regFrom));
            writeRegister(regTo, value);
            ip += 3;
            break;
        case Opcodes.ADD_REGADDRESS_TO_REG:
            break;
        default:
            throw new RuntimeException("²Ù×÷Ö¸Áî´íÎó£º" + instr);
        }
    }

    private int processResult(int value) {
        zero = false;
        carry = false;
        if (value >= 256) {
            carry = true;
            value %= 256;
        } else if (value == 0) {
            zero = true;
        } else if (value < 0) {
            carry = true;
            value = 255 - (-value) % 256;
        }
        return value;
    }

    private void writeRegister(int regTo, int value) {
        // TODO Auto-generated method stub
    }

    private int readRegister(int regTo) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
    }
}
