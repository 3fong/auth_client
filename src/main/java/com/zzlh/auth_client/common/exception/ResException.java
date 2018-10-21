package com.zzlh.auth_client.common.exception;

import com.zzlh.auth_client.common.constant.HTTPCode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class ResException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private String msg;

    public ResException() {  
        super();
    }
    
    public ResException(String msg) {
        this.code = HTTPCode.ERROR;
    }
    
    public ResException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
}
