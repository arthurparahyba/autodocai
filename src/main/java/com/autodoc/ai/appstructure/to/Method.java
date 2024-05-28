package com.autodoc.ai.appstructure.to;

import java.util.ArrayList;
import java.util.List;

public record Method(
        String nome,
        List<Param> parametros,
//        List<Call> calls,
        Dependency retorno
) {}
