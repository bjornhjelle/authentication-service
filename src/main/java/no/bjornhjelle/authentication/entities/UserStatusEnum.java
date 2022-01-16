package no.bjornhjelle.authentication.entities;

import no.bjornhjelle.authentication.entities.enumtypes.LabeledEnum;

public enum UserStatusEnum implements LabeledEnum {

    CREATED("created"),
    ACTIVATED("activated"),
    INACTIVE("inactive");

    private final String label;

    UserStatusEnum(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
