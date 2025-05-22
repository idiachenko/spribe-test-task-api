package enums;

import lombok.Getter;

@Getter
public enum Editors {
    SUPERVISOR("supervisorLogin"),
    USER("userLogin"),
    ADMIN("adminLogin"),
    UNKNOWN("unknown");

    private final String editor;

    Editors(String editor) {
        this.editor = editor;
    }

}
