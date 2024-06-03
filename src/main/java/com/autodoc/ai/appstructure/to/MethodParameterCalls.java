package com.autodoc.ai.appstructure.to;

import java.util.List;

public record MethodParameterCalls(String methodName, List<CalledParameterMethods> parameterMethodCalls) {
}
