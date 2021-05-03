package cn.plutowu.exception;

import cn.plutowu.result.CodeMsg;

/**
 * 全局异常
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
public class GlobalException extends RuntimeException{
    private static final long servialVersionUID = 1L;

    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }
}
