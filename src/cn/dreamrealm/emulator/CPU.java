package cn.dreamrealm.emulator;

public class CPU {

    private static final int minSP = 0;

    private static final int maxSP = 231;

    private static final Memory memory = new Memory();

    // general purpose registers
    int[] gpr = { 0, 0, 0, 0 };

    // instruction pointer
    int ip;

    // stack pointer
    int sp;

    boolean zero;

    boolean carry;

    boolean fault;

    public void reset() {
        for (int i = 0; i < gpr.length; i++) {
            gpr[i] = 0;
        }
        ip = 0;
        sp = 0;
        zero = false;
        carry = false;
        fault = false;
    }

    public boolean step() {
        try {
            int regTo, regFrom, memTo, memFrom, number;
            if (fault) {
                throw new RuntimeException("FAULT. Reset to continue.");
            }
            int instr = memory.load(ip);
            switch (instr) {
            case Opcodes.NONE:
                return false; // Abort step
            case Opcodes.MOV_REG_TO_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                regFrom = checkGPR_SP(memory.load(++ip));
                setGPR_SP(regTo, getGPR_SP(regFrom));
                ip++;
                break;
            case Opcodes.MOV_ADDRESS_TO_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                memFrom = memory.load(++ip);
                setGPR_SP(regTo, memory.load(memFrom));
                ip++;
                break;
            case Opcodes.MOV_REGADDRESS_TO_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                regFrom = memory.load(++ip);
                setGPR_SP(regTo, memory.load(indirectRegisterAddress(regFrom)));
                ip++;
                break;
            case Opcodes.MOV_REG_TO_ADDRESS:
                memTo = memory.load(++ip);
                regFrom = checkGPR_SP(memory.load(++ip));
                memory.store(memTo, getGPR_SP(regFrom));
                ip++;
                break;
            case Opcodes.MOV_REG_TO_REGADDRESS:
                regTo = memory.load(++ip);
                regFrom = checkGPR_SP(memory.load(++ip));
                memory.store(indirectRegisterAddress(regTo), getGPR_SP(regFrom));
                ip++;
                break;
            case Opcodes.MOV_NUMBER_TO_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                number = memory.load(++ip);
                setGPR_SP(regTo, number);
                ip++;
                break;
            case Opcodes.MOV_NUMBER_TO_ADDRESS:
                memTo = memory.load(++ip);
                number = memory.load(++ip);
                memory.store(memTo, number);
                ip++;
                break;
            case Opcodes.MOV_NUMBER_TO_REGADDRESS:
                regTo = memory.load(++ip);
                number = memory.load(++ip);
                memory.store(indirectRegisterAddress(regTo), number);
                ip++;
                break;
            case Opcodes.ADD_REG_TO_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                regFrom = checkGPR_SP(memory.load(++ip));
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) + getGPR_SP(regFrom)));
                ip++;
                break;
            case Opcodes.ADD_REGADDRESS_TO_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                regFrom = memory.load(++ip);
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) + memory.load(indirectRegisterAddress(regFrom))));
                ip++;
                break;
            case Opcodes.ADD_ADDRESS_TO_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                memFrom = memory.load(++ip);
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) + memory.load(memFrom)));
                ip++;
                break;
            case Opcodes.ADD_NUMBER_TO_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                number = memory.load(++ip);
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) + number));
                ip++;
                break;
            case Opcodes.SUB_REG_FROM_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                regFrom = checkGPR_SP(memory.load(++ip));
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) - gpr[regFrom]));
                ip++;
                break;
            case Opcodes.SUB_REGADDRESS_FROM_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                regFrom = memory.load(++ip);
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) - memory.load(indirectRegisterAddress(regFrom))));
                ip++;
                break;
            case Opcodes.SUB_ADDRESS_FROM_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                memFrom = memory.load(++ip);
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) - memory.load(memFrom)));
                ip++;
                break;
            case Opcodes.SUB_NUMBER_FROM_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                number = memory.load(++ip);
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) - number));
                ip++;
                break;
            case Opcodes.INC_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) + 1));
                ip++;
                break;
            case Opcodes.DEC_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                setGPR_SP(regTo, checkOperation(getGPR_SP(regTo) - 1));
                ip++;
                break;
            case Opcodes.CMP_REG_WITH_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                regFrom = checkGPR_SP(memory.load(++ip));
                checkOperation(getGPR_SP(regTo) - getGPR_SP(regFrom));
                ip++;
                break;
            case Opcodes.CMP_REGADDRESS_WITH_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                regFrom = memory.load(++ip);
                checkOperation(getGPR_SP(regTo) - memory.load(indirectRegisterAddress(regFrom)));
                ip++;
                break;
            case Opcodes.CMP_ADDRESS_WITH_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                memFrom = memory.load(++ip);
                checkOperation(getGPR_SP(regTo) - memory.load(memFrom));
                ip++;
                break;
            case Opcodes.CMP_NUMBER_WITH_REG:
                regTo = checkGPR_SP(memory.load(++ip));
                number = memory.load(++ip);
                checkOperation(getGPR_SP(regTo) - number);
                ip++;
                break;
            case Opcodes.JMP_REGADDRESS:
                regTo = checkGPR(memory.load(++ip));
                jump(gpr[regTo]);
                break;
            case Opcodes.JMP_ADDRESS:
                number = memory.load(++ip);
                jump(number);
                break;
            case Opcodes.JC_REGADDRESS:
                regTo = checkGPR(memory.load(++ip));
                if (carry) {
                    jump(gpr[regTo]);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JC_ADDRESS:
                number = memory.load(++ip);
                if (carry) {
                    jump(number);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JNC_REGADDRESS:
                regTo = checkGPR(memory.load(++ip));
                if (!carry) {
                    jump(gpr[regTo]);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JNC_ADDRESS:
                number = memory.load(++ip);
                if (!carry) {
                    jump(number);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JZ_REGADDRESS:
                regTo = checkGPR(memory.load(++ip));
                if (zero) {
                    jump(gpr[regTo]);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JZ_ADDRESS:
                number = memory.load(++ip);
                if (zero) {
                    jump(number);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JNZ_REGADDRESS:
                regTo = checkGPR(memory.load(++ip));
                if (!zero) {
                    jump(gpr[regTo]);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JNZ_ADDRESS:
                number = memory.load(++ip);
                if (!zero) {
                    jump(number);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JA_REGADDRESS:
                regTo = checkGPR(memory.load(++ip));
                if (!zero && !carry) {
                    jump(gpr[regTo]);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JA_ADDRESS:
                number = memory.load(++ip);
                if (!zero && !carry) {
                    jump(number);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JNA_REGADDRESS: // JNA REG
                regTo = checkGPR(memory.load(++ip));
                if (zero || carry) {
                    jump(gpr[regTo]);
                } else {
                    ip++;
                }
                break;
            case Opcodes.JNA_ADDRESS:
                number = memory.load(++ip);
                if (zero || carry) {
                    jump(number);
                } else {
                    ip++;
                }
                break;
            case Opcodes.PUSH_REG:
                regFrom = checkGPR(memory.load(++ip));
                push(gpr[regFrom]);
                ip++;
                break;
            case Opcodes.PUSH_REGADDRESS:
                regFrom = memory.load(++ip);
                push(memory.load(indirectRegisterAddress(regFrom)));
                ip++;
                break;
            case Opcodes.PUSH_ADDRESS:
                memFrom = memory.load(++ip);
                push(memory.load(memFrom));
                ip++;
                break;
            case Opcodes.PUSH_NUMBER:
                number = memory.load(++ip);
                push(number);
                ip++;
                break;
            case Opcodes.POP_REG:
                regTo = checkGPR(memory.load(++ip));
                gpr[regTo] = pop();
                ip++;
                break;
            case Opcodes.CALL_REGADDRESS:
                regTo = checkGPR(memory.load(++ip));
                push(ip + 1);
                jump(gpr[regTo]);
                break;
            case Opcodes.CALL_ADDRESS:
                number = memory.load(++ip);
                push(ip + 1);
                jump(number);
                break;
            case Opcodes.RET:
                jump(pop());
                break;
            case Opcodes.MUL_REG: // A = A * REG
                regFrom = checkGPR(memory.load(++ip));
                gpr[0] = checkOperation(gpr[0] * gpr[regFrom]);
                ip++;
                break;
            case Opcodes.MUL_REGADDRESS: // A = A * [REG]
                regFrom = memory.load(++ip);
                gpr[0] = checkOperation(gpr[0] * memory.load(indirectRegisterAddress(regFrom)));
                ip++;
                break;
            case Opcodes.MUL_ADDRESS: // A = A * [NUMBER]
                memFrom = memory.load(++ip);
                gpr[0] = checkOperation(gpr[0] * memory.load(memFrom));
                ip++;
                break;
            case Opcodes.MUL_NUMBER: // A = A * NUMBER
                number = memory.load(++ip);
                gpr[0] = checkOperation(gpr[0] * number);
                ip++;
                break;
            case Opcodes.DIV_REG: // A = A / REG
                regFrom = checkGPR(memory.load(++ip));
                gpr[0] = checkOperation(division(gpr[regFrom]));
                ip++;
                break;
            case Opcodes.DIV_REGADDRESS: // A = A / [REG]
                regFrom = memory.load(++ip);
                gpr[0] = checkOperation(division(memory.load(indirectRegisterAddress(regFrom))));
                ip++;
                break;
            case Opcodes.DIV_ADDRESS: // A = A / [NUMBER]
                memFrom = memory.load(++ip);
                gpr[0] = checkOperation(division(memory.load(memFrom)));
                ip++;
                break;
            case Opcodes.DIV_NUMBER: // A = A / NUMBER
                number = memory.load(++ip);
                gpr[0] = checkOperation(division(number));
                ip++;
                break;
            case Opcodes.AND_REG_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = checkGPR(memory.load(++ip));
                gpr[regTo] = checkOperation(gpr[regTo] & gpr[regFrom]);
                ip++;
                break;
            case Opcodes.AND_REGADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] & memory.load(indirectRegisterAddress(regFrom)));
                ip++;
                break;
            case Opcodes.AND_ADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                memFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] & memory.load(memFrom));
                ip++;
                break;
            case Opcodes.AND_NUMBER_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                number = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] & number);
                ip++;
                break;
            case Opcodes.OR_REG_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = checkGPR(memory.load(++ip));
                gpr[regTo] = checkOperation(gpr[regTo] | gpr[regFrom]);
                ip++;
                break;
            case Opcodes.OR_REGADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] | memory.load(indirectRegisterAddress(regFrom)));
                ip++;
                break;
            case Opcodes.OR_ADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                memFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] | memory.load(memFrom));
                ip++;
                break;
            case Opcodes.OR_NUMBER_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                number = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] | number);
                ip++;
                break;
            case Opcodes.XOR_REG_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = checkGPR(memory.load(++ip));
                gpr[regTo] = checkOperation(gpr[regTo] ^ gpr[regFrom]);
                ip++;
                break;
            case Opcodes.XOR_REGADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] ^ memory.load(indirectRegisterAddress(regFrom)));
                ip++;
                break;
            case Opcodes.XOR_ADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                memFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] ^ memory.load(memFrom));
                ip++;
                break;
            case Opcodes.XOR_NUMBER_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                number = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] ^ number);
                ip++;
                break;
            case Opcodes.NOT_REG:
                regTo = checkGPR(memory.load(++ip));
                gpr[regTo] = checkOperation(~gpr[regTo]);
                ip++;
                break;
            case Opcodes.SHL_REG_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = checkGPR(memory.load(++ip));
                gpr[regTo] = checkOperation(gpr[regTo] << gpr[regFrom]);
                ip++;
                break;
            case Opcodes.SHL_REGADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] << memory.load(indirectRegisterAddress(regFrom)));
                ip++;
                break;
            case Opcodes.SHL_ADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                memFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] << memory.load(memFrom));
                ip++;
                break;
            case Opcodes.SHL_NUMBER_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                number = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] << number);
                ip++;
                break;
            case Opcodes.SHR_REG_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = checkGPR(memory.load(++ip));
                gpr[regTo] = checkOperation(gpr[regTo] >>> gpr[regFrom]);
                ip++;
                break;
            case Opcodes.SHR_REGADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                regFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] >>> memory.load(indirectRegisterAddress(regFrom)));
                ip++;
                break;
            case Opcodes.SHR_ADDRESS_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                memFrom = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] >>> memory.load(memFrom));
                ip++;
                break;
            case Opcodes.SHR_NUMBER_WITH_REG:
                regTo = checkGPR(memory.load(++ip));
                number = memory.load(++ip);
                gpr[regTo] = checkOperation(gpr[regTo] >>> number);
                ip++;
                break;
            default:
                throw new RuntimeException("Invalid op code: " + instr);
            }
            return true;
        } catch (Exception e) {
            fault = true;
            throw e;
        }
    }

    private int checkGPR(int reg) {
        if (reg < 0 || reg >= gpr.length) {
            throw new RuntimeException("Invalid register: " + reg);
        } else {
            return reg;
        }
    }

    private int checkGPR_SP(int reg) {
        if (reg < 0 || reg >= 1 + gpr.length) {
            throw new RuntimeException("Invalid register: " + reg);
        } else {
            return reg;
        }
    }

    private void setGPR_SP(int reg, int value) {
        if (reg >= 0 && reg < gpr.length) {
            gpr[reg] = value;
        } else if (reg == gpr.length) {
            sp = value;
            // Not likely to happen, since we always get here after
            // checkOperation
            if (sp < minSP) {
                throw new RuntimeException("Stack overflow");
            } else if (sp > maxSP) {
                throw new RuntimeException("Stack underflow");
            }
        } else {
            throw new RuntimeException("Invalid register: " + reg);
        }
    }

    private int getGPR_SP(int reg) {
        if (reg >= 0 && reg < gpr.length) {
            return gpr[reg];
        } else if (reg == gpr.length) {
            return sp;
        } else {
            throw new RuntimeException("Invalid register: " + reg);
        }
    }

    private int indirectRegisterAddress(int value) {
        int reg = value % 8;
        int base;
        if (reg < gpr.length) {
            base = gpr[reg];
        } else {
            base = sp;
        }
        int offset = value / 8;
        if (offset > 15) {
            offset = offset - 32;
        }
        return base + offset;
    }

    private int checkOperation(int value) {
        zero = false;
        carry = false;
        if (value >= 256) {
            carry = true;
            value = value % 256;
        } else if (value == 0) {
            zero = true;
        } else if (value < 0) {
            carry = true;
            value = 256 - (-value) % 256;
        }
        return value;
    }

    private void jump(int newIP) {
        if (newIP < 0 || newIP >= memory.data.length) {
            throw new RuntimeException("IP outside memory");
        } else {
            ip = newIP;
        }
    }

    private int pop() {
        int value = memory.load(++sp);
        if (sp > maxSP) {
            throw new RuntimeException("Stack underflow");
        }
        return value;
    }

    private void push(int value) {
        memory.store(sp--, value);
        if (sp < minSP) {
            throw new RuntimeException("Stack overflow");
        }
    }

    private int division(int divisor) {
        if (divisor == 0) {
            throw new RuntimeException("Division by 0");
        }
        return gpr[0] / divisor;
    }
}
