package com.github.vspro.cg.template;

import com.github.vspro.cg.codegen.GeneratedFile;

public interface EngineClient {

	void init();

	<T extends GeneratedFile> String render(T gf);

}
