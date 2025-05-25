package hnqd.aparmentmanager.common.Enum;

import lombok.Getter;

@Getter
public enum EKey {
    SECRET_KEY("my_secret_key");

    private final String key;

    EKey(String mySecretKey) {
        this.key = mySecretKey;
    }
}
