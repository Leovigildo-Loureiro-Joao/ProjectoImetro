package com.imetro.persistence.repository;

import java.util.Objects;

public final class SqlIdentifiers {

    private static final String IDENT = "[A-Za-z_][A-Za-z0-9_]*";
    private static final String QUALIFIED = IDENT + "(\\." + IDENT + ")*";

    private SqlIdentifiers() {
    }

    public static String requireSafeQualifiedName(String identifier) {
        Objects.requireNonNull(identifier, "identifier");
        if (!identifier.matches(QUALIFIED)) {
            throw new IllegalArgumentException("Unsafe SQL identifier: " + identifier);
        }
        return identifier;
    }
}

