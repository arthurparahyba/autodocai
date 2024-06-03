package com.autodoc.ai.appstructure.to;

import com.autodoc.ai.appstructure.repository.CodeEntity;
import com.autodoc.ai.appstructure.repository.Layer;

import java.util.List;

public record ApplicationByDocument(Long appId, Layer layer, List<CodeEntity> entities) {
}
