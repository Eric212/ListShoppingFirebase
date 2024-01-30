package com.ericsospedra.listshoppingfirebase.utils;

import android.content.Context;
import android.content.res.Resources;

public class ResourceChecker {

    public static boolean existeRecursoPorNombre(Context context, String nombreRecurso, String tipoRecurso) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(nombreRecurso, tipoRecurso, context.getPackageName());

        return resourceId != 0; // Si resourceId es 0, el recurso no existe; de lo contrario, existe.
    }
}
