package com.github.vspro.cg.internal;

public class FullyQualifiedJavaType implements Comparable<FullyQualifiedJavaType> {



	private static final String JAVA_LANG = "java.lang";

	/** The short name without any generic arguments. */
	private String baseShortName;

	/** The fully qualified name without any generic arguments. */
	private String baseQualifiedName;

	private boolean explicitlyImported;

	private String packageName;
	
	private boolean primitive;

	public FullyQualifiedJavaType(String fullTypeSpecification) {
		simpleParse(fullTypeSpecification);
	}

	private void simpleParse(String typeSpecification) {
		baseQualifiedName = typeSpecification.trim();
		if (baseQualifiedName.contains(".")) { 
			packageName = getPackage(baseQualifiedName);
			baseShortName = baseQualifiedName
					.substring(packageName.length() + 1);
			int index = baseShortName.lastIndexOf('.');
			if (index != -1) {
				baseShortName = baseShortName.substring(index + 1);
			}

			if (JAVA_LANG.equals(packageName)) { 
				explicitlyImported = false;
			} else {
				explicitlyImported = true;
			}
		}else {

			baseShortName = baseQualifiedName;
			explicitlyImported = false;
			packageName = ""; 

			if ("byte".equals(baseQualifiedName)) { 
				primitive = true;
			} else if ("short".equals(baseQualifiedName)) { 
				primitive = true;
			} else if ("int".equals(baseQualifiedName)) { 
				primitive = true;
			} else if ("long".equals(baseQualifiedName)) { 
				primitive = true;
			} else if ("char".equals(baseQualifiedName)) { 
				primitive = true;
				
			} else if ("float".equals(baseQualifiedName)) { 
				primitive = true;
			} else if ("double".equals(baseQualifiedName)) { 
				primitive = true;
			} else if ("boolean".equals(baseQualifiedName)) { 
				primitive = true;
			} else {
				primitive = false;
			}
//			throw new RuntimeException(getString("JavaMemberParameterParseError.0"));
		}

	}

	private static String getPackage(String baseQualifiedName) {
		int index = baseQualifiedName.lastIndexOf('.');
		return baseQualifiedName.substring(0, index);
	}

	public String getFullyQualifiedName() {
		StringBuilder sb = new StringBuilder();
		sb.append(baseQualifiedName);
		return sb.toString();
	}

	public String getBaseShortName() {
		return baseShortName;
	}

	@Override
	public int compareTo(FullyQualifiedJavaType other) {
		return getFullyQualifiedName().compareTo(other.getFullyQualifiedName());
	}
}
