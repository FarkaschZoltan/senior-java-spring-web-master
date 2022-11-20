package hu.ponte.hr.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author zoltan
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageMeta {
	private String id;
	private String name;
	private String path;
	private String mimeType;
	private long size;
	private String digitalSign;
}
