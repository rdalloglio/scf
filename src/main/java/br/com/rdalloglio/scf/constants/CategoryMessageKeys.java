package br.com.rdalloglio.scf.constants;

public final class CategoryMessageKeys {

    private CategoryMessageKeys() {
        // evitar instância
    }

    public static final String INVALID_REQUEST = "error.invalid-request";
    
    public static final String NOT_FOUND = "category.not-found";
    public static final String NAME_REQUIRED = "category.name.not-blank";
    public static final String TYPE_REQUIRED = "category.type.not-null";
    public static final String TYPE_INVALID = "category.type.invalid";
}