package cn.dreamrealm.emulator;

public interface Opcodes {

    public static final byte NONE = 0;

    public static final byte MOV_REG_TO_REG = 1, MOV_ADDRESS_TO_REG = 2, MOV_REGADDRESS_TO_REG = 3,
            MOV_REG_TO_ADDRESS = 4, MOV_REG_TO_REGADDRESS = 5, MOV_NUMBER_TO_REG = 6, MOV_NUMBER_TO_ADDRESS = 7,
            MOV_NUMBER_TO_REGADDRESS = 8;

    public static final byte ADD_REG_TO_REG = 10, ADD_REGADDRESS_TO_REG = 11, ADD_ADDRESS_TO_REG = 12,
            ADD_NUMBER_TO_REG = 13;

    public static final byte SUB_REG_FROM_REG = 14, SUB_REGADDRESS_FROM_REG = 15, SUB_ADDRESS_FROM_REG = 16,
            SUB_NUMBER_FROM_REG = 17;

    public static final byte INC_REG = 18, DEC_REG = 19;

    public static final byte CMP_REG_WITH_REG = 20, CMP_REGADDRESS_WITH_REG = 21, CMP_ADDRESS_WITH_REG = 22,
            CMP_NUMBER_WITH_REG = 23;

    public static final byte JMP_REGADDRESS = 30, JMP_ADDRESS = 31;

    public static final byte JC_REGADDRESS = 32, JC_ADDRESS = 33;

    public static final byte JNC_REGADDRESS = 34, JNC_ADDRESS = 35;

    public static final byte JZ_REGADDRESS = 36, JZ_ADDRESS = 37;

    public static final byte JNZ_REGADDRESS = 38, JNZ_ADDRESS = 39;

    public static final byte JA_REGADDRESS = 40, JA_ADDRESS = 41;

    public static final byte JNA_REGADDRESS = 42, JNA_ADDRESS = 43;

    public static final byte PUSH_REG = 50, PUSH_REGADDRESS = 51, PUSH_ADDRESS = 52, PUSH_NUMBER = 53;

    public static final byte POP_REG = 54;

    public static final byte CALL_REGADDRESS = 55, CALL_ADDRESS = 56;

    public static final byte RET = 57;

    public static final byte MUL_REG = 60, MUL_REGADDRESS = 61, MUL_ADDRESS = 62, MUL_NUMBER = 63;

    public static final byte DIV_REG = 64, DIV_REGADDRESS = 65, DIV_ADDRESS = 66, DIV_NUMBER = 67;

    public static final byte AND_REG_WITH_REG = 70, AND_REGADDRESS_WITH_REG = 71, AND_ADDRESS_WITH_REG = 72,
            AND_NUMBER_WITH_REG = 73;

    public static final byte OR_REG_WITH_REG = 74, OR_REGADDRESS_WITH_REG = 75, OR_ADDRESS_WITH_REG = 76,
            OR_NUMBER_WITH_REG = 77;

    public static final byte XOR_REG_WITH_REG = 78, XOR_REGADDRESS_WITH_REG = 79, XOR_ADDRESS_WITH_REG = 80,
            XOR_NUMBER_WITH_REG = 81;

    public static final byte NOT_REG = 82;

    public static final byte SHL_REG_WITH_REG = 90, SHL_REGADDRESS_WITH_REG = 91, SHL_ADDRESS_WITH_REG = 92,
            SHL_NUMBER_WITH_REG = 93, SHR_REG_WITH_REG = 94;

    public static final byte SHR_REGADDRESS_WITH_REG = 95, SHR_ADDRESS_WITH_REG = 96, SHR_NUMBER_WITH_REG = 97;
}
