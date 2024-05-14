package com.autodoc.ai.appstructure.to;

import java.util.ArrayList;
import java.util.List;

public record Method(
        String name,
        List<Param> params,
        List<Call> calls,
        Dependency returns
) {}
