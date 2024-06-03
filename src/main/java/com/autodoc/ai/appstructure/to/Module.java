package com.autodoc.ai.appstructure.to;

import java.util.List;

public record Module(String moduleName, List<ModuleClass> classes){}