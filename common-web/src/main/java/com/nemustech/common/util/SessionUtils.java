package com.nemustech.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SessionUtils {
    public enum SessionType {
        HSC("HSC"),
        PCM("PCM"),
        HPM("HPM"),
        UNKNOWN("UNK"),
        NOTHING("NOT");
        
        private String strValue;
        
        SessionType(String strValue) {
            this.strValue = strValue;
        }
        
        public String toString() {
    		return StringUtil.toString(this);
        }
    }
    
    public static final String getLoginedId() {
        return StringUtils.upperCase(
            SecurityContextHolder.getContext().getAuthentication().getName());
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getLoginInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return (T) auth.getDetails();
    }
    
    public static String getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();
        
        if (StringUtils.isEmpty(role))
            return "";
        else
            return role.substring(role.indexOf("[") + 1, role.indexOf("]"));
    }
    
    public static SessionType getSessionType() {
        Object loginInfo = getLoginInfo();
        
        if (loginInfo == null)
            return SessionType.NOTHING;
        
//        if (loginInfo instanceof HscLoginInfo)
//            return SessionType.HSC;
//        else if (loginInfo instanceof PcmLoginInfo) {
            String role = getRole();
            
            if (StringUtils.isEmpty(role)) {
                return SessionType.NOTHING;
            } else if (StringUtils.contains(role, "ROLE_PCM")) {
                return SessionType.PCM;
            } else if (StringUtils.contains(role, "ROLE_HPM")) {
                return SessionType.HPM;
            }
//        }
        
        return SessionType.UNKNOWN;
    }
}
