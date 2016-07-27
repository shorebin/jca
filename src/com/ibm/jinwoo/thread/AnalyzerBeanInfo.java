package com.ibm.jinwoo.thread;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.io.File;
import java.lang.reflect.Method;

public class AnalyzerBeanInfo extends SimpleBeanInfo {
	public MethodDescriptor aFMenuItem_ActionPerformed_javaawteventActionEventMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { ActionEvent.class };

				aMethod = getBeanClass().getMethod("aFMenuItem_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "aFMenuItem_ActionPerformed", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("actionEvent");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor browseButton_ActionPerformed_javaawteventActionEventMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { ActionEvent.class };

				aMethod = getBeanClass().getMethod("browseButton_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "browseButton_ActionPerformed", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("actionEvent");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor changeButton_ActionPerformed_javaawteventActionEventMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { ActionEvent.class };

				aMethod = getBeanClass().getMethod("changeButton_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "changeButton_ActionPerformed", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("actionEvent");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor colorComboBox_ActionPerformed_javaawteventActionEventMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { ActionEvent.class };

				aMethod = getBeanClass().getMethod("colorComboBox_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "colorComboBox_ActionPerformed", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("actionEvent");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public PropertyDescriptor componentOrientationPropertyDescriptor() {
		PropertyDescriptor aDescriptor = null;
		try {
			try {
				Method aGetMethod = null;
				try {
					Class<?>[] aGetMethodParameterTypes = new Class[0];
					aGetMethod = getBeanClass().getMethod("getComponentOrientation", aGetMethodParameterTypes);
				} catch (Throwable exception) {
					handleException(exception);
					aGetMethod = findMethod(getBeanClass(), "getComponentOrientation", 0);
				}
				Method aSetMethod = null;
				try {
					Class<?>[] aSetMethodParameterTypes = { ComponentOrientation.class };

					aSetMethod = getBeanClass().getMethod("setComponentOrientation", aSetMethodParameterTypes);
				} catch (Throwable exception) {
					handleException(exception);
					aSetMethod = findMethod(getBeanClass(), "setComponentOrientation", 1);
				}
				aDescriptor = new PropertyDescriptor("componentOrientation", aGetMethod, aSetMethod);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new PropertyDescriptor("componentOrientation", getBeanClass());
			}

			aDescriptor.setValue("enumerationValues",
					new Object[] { "UNKNOWN", ComponentOrientation.UNKNOWN, "java.awt.ComponentOrientation.UNKNOWN",
							"LEFT_TO_RIGHT", ComponentOrientation.LEFT_TO_RIGHT,
							"java.awt.ComponentOrientation.LEFT_TO_RIGHT", "RIGHT_TO_LEFT",
							ComponentOrientation.RIGHT_TO_LEFT, "java.awt.ComponentOrientation.RIGHT_TO_LEFT" });
		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor detailTextPaneSetText_javalangStringMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { String.class };

				aMethod = getBeanClass().getMethod("detailTextPaneSetText", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "detailTextPaneSetText", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("string");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor durationMenuItem_ActionPerformed_javaawteventActionEventMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { ActionEvent.class };

				aMethod = getBeanClass().getMethod("durationMenuItem_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "durationMenuItem_ActionPerformed", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("actionEvent");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public static Method findMethod(Class<?> aClass, String methodName, int parameterCount) {
		try {
			Method[] methods = aClass.getMethods();
			for (int index = 0; index < methods.length; index++) {
				Method method = methods[index];
				if ((method.getParameterTypes().length == parameterCount) && (method.getName().equals(methodName)))
					return method;
			}
		} catch (Throwable exception) {
			return null;
		}
		return null;
	}

	public MethodDescriptor gC_ListMenuItem_ActionPerformedMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = new Class[0];
				aMethod = getBeanClass().getMethod("gC_ListMenuItem_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "gC_ListMenuItem_ActionPerformed", 0);
			}
			try {
				ParameterDescriptor[] aParameterDescriptors = new ParameterDescriptor[0];
				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor gCAnalyzer_WindowClosing_javaawteventWindowEventMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { WindowEvent.class };

				aMethod = getBeanClass().getMethod("gCAnalyzer_WindowClosing", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "gCAnalyzer_WindowClosing", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("windowEvent");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public BeanInfo[] getAdditionalBeanInfo() {
		Class<?> superClass;
		BeanInfo superBeanInfo = null;
		try {
			superClass = getBeanDescriptor().getBeanClass().getSuperclass();
		} catch (Throwable exception) {
			return null;
		}
		try {
			superBeanInfo = Introspector.getBeanInfo(superClass);
		} catch (IntrospectionException localIntrospectionException) {
		}
		if (superBeanInfo != null) {
			BeanInfo[] ret = new BeanInfo[1];
			ret[0] = superBeanInfo;
			return ret;
		}
		return null;
	}

	public static Class<?> getBeanClass() {
		return Analyzer.class;
	}

	public static String getBeanClassName() {
		return "com.ibm.jinwoo.thread.Analyzer";
	}

	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor aDescriptor = null;
		try {
			aDescriptor = new BeanDescriptor(Analyzer.class);
		} catch (Throwable localThrowable) {
		}

		return aDescriptor;
	}

	public EventSetDescriptor[] getEventSetDescriptors() {
		try {
			return new EventSetDescriptor[0];
		} catch (Throwable exception) {
			handleException(exception);
		}
		return null;
	}

	public MethodDescriptor getInputStream_javaioFileMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { File.class };

				aMethod = getBeanClass().getMethod("getInputStream", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "getInputStream", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("file");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor getJDesktopPane1MethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = new Class[0];
				aMethod = getBeanClass().getMethod("getJDesktopPane1", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "getJDesktopPane1", 0);
			}
			try {
				ParameterDescriptor[] aParameterDescriptors = new ParameterDescriptor[0];
				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor[] getMethodDescriptors() {
		try {
			return new MethodDescriptor[] { aFMenuItem_ActionPerformed_javaawteventActionEventMethodDescriptor(),
					browseButton_ActionPerformed_javaawteventActionEventMethodDescriptor(),
					changeButton_ActionPerformed_javaawteventActionEventMethodDescriptor(),
					colorComboBox_ActionPerformed_javaawteventActionEventMethodDescriptor(),
					detailTextPaneSetText_javalangStringMethodDescriptor(),
					durationMenuItem_ActionPerformed_javaawteventActionEventMethodDescriptor(),
					gC_ListMenuItem_ActionPerformedMethodDescriptor(),
					gCAnalyzer_WindowClosing_javaawteventWindowEventMethodDescriptor(),
					getInputStream_javaioFileMethodDescriptor(), getJDesktopPane1MethodDescriptor(),
					getThreadDumpTableMethodDescriptor(), graph_ViewMenuItem_ActionPerformedMethodDescriptor(),
					handleException_javalangThrowableMethodDescriptor(),
					help_TopicsMenuItem_ActionPerformed_javaawteventActionEventMethodDescriptor(),
					main_javalangString__MethodDescriptor(), openMenuItem_ActionPerformedMethodDescriptor(),
					showAboutBoxMethodDescriptor(),
					usageMenuItem_ActionPerformed_javaawteventActionEventMethodDescriptor(),
					viewConsoleMethodDescriptor(), viewStatusBarMethodDescriptor() };
		} catch (Throwable exception) {
			handleException(exception);
		}
		return null;
	}

	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			return new PropertyDescriptor[] { componentOrientationPropertyDescriptor(),
					JDesktopPane1PropertyDescriptor(), threadDumpTablePropertyDescriptor() };
		} catch (Throwable exception) {
			handleException(exception);
		}
		return null;
	}

	public MethodDescriptor getThreadDumpTableMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = new Class[0];
				aMethod = getBeanClass().getMethod("getThreadDumpTable", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "getThreadDumpTable", 0);
			}
			try {
				ParameterDescriptor[] aParameterDescriptors = new ParameterDescriptor[0];
				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor graph_ViewMenuItem_ActionPerformedMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = new Class[0];
				aMethod = getBeanClass().getMethod("graph_ViewMenuItem_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "graph_ViewMenuItem_ActionPerformed", 0);
			}
			try {
				ParameterDescriptor[] aParameterDescriptors = new ParameterDescriptor[0];
				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	private void handleException(Throwable exception) {
	}

	public MethodDescriptor handleException_javalangThrowableMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { Throwable.class };

				aMethod = getBeanClass().getMethod("handleException", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "handleException", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("exception");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor help_TopicsMenuItem_ActionPerformed_javaawteventActionEventMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { ActionEvent.class };

				aMethod = getBeanClass().getMethod("help_TopicsMenuItem_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "help_TopicsMenuItem_ActionPerformed", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("actionEvent");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public PropertyDescriptor JDesktopPane1PropertyDescriptor() {
		PropertyDescriptor aDescriptor = null;
		try {
			try {
				Method aGetMethod = null;
				try {
					Class<?>[] aGetMethodParameterTypes = new Class[0];
					aGetMethod = getBeanClass().getMethod("getJDesktopPane1", aGetMethodParameterTypes);
				} catch (Throwable exception) {
					handleException(exception);
					aGetMethod = findMethod(getBeanClass(), "getJDesktopPane1", 0);
				}
				Method aSetMethod = null;
				aDescriptor = new PropertyDescriptor("JDesktopPane1", aGetMethod, aSetMethod);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new PropertyDescriptor("JDesktopPane1", getBeanClass());
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor main_javalangString__MethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { String[].class };

				aMethod = getBeanClass().getMethod("main", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "main", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("args");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor openMenuItem_ActionPerformedMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = new Class[0];
				aMethod = getBeanClass().getMethod("openMenuItem_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "openMenuItem_ActionPerformed", 0);
			}
			try {
				ParameterDescriptor[] aParameterDescriptors = new ParameterDescriptor[0];
				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor showAboutBoxMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = new Class[0];
				aMethod = getBeanClass().getMethod("showAboutBox", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "showAboutBox", 0);
			}
			try {
				ParameterDescriptor[] aParameterDescriptors = new ParameterDescriptor[0];
				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public PropertyDescriptor threadDumpTablePropertyDescriptor() {
		PropertyDescriptor aDescriptor = null;
		try {
			try {
				Method aGetMethod = null;
				try {
					Class<?>[] aGetMethodParameterTypes = new Class[0];
					aGetMethod = getBeanClass().getMethod("getThreadDumpTable", aGetMethodParameterTypes);
				} catch (Throwable exception) {
					handleException(exception);
					aGetMethod = findMethod(getBeanClass(), "getThreadDumpTable", 0);
				}
				Method aSetMethod = null;
				aDescriptor = new PropertyDescriptor("threadDumpTable", aGetMethod, aSetMethod);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new PropertyDescriptor("threadDumpTable", getBeanClass());
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor usageMenuItem_ActionPerformed_javaawteventActionEventMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = { ActionEvent.class };

				aMethod = getBeanClass().getMethod("usageMenuItem_ActionPerformed", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "usageMenuItem_ActionPerformed", 1);
			}
			try {
				ParameterDescriptor aParameterDescriptor1 = new ParameterDescriptor();
				aParameterDescriptor1.setName("arg1");
				aParameterDescriptor1.setDisplayName("actionEvent");
				ParameterDescriptor[] aParameterDescriptors = { aParameterDescriptor1 };

				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor viewConsoleMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = new Class[0];
				aMethod = getBeanClass().getMethod("viewConsole", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "viewConsole", 0);
			}
			try {
				ParameterDescriptor[] aParameterDescriptors = new ParameterDescriptor[0];
				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}

	public MethodDescriptor viewStatusBarMethodDescriptor() {
		MethodDescriptor aDescriptor = null;
		try {
			Method aMethod = null;
			try {
				Class<?>[] aParameterTypes = new Class[0];
				aMethod = getBeanClass().getMethod("viewStatusBar", aParameterTypes);
			} catch (Throwable exception) {
				handleException(exception);
				aMethod = findMethod(getBeanClass(), "viewStatusBar", 0);
			}
			try {
				ParameterDescriptor[] aParameterDescriptors = new ParameterDescriptor[0];
				aDescriptor = new MethodDescriptor(aMethod, aParameterDescriptors);
			} catch (Throwable exception) {
				handleException(exception);
				aDescriptor = new MethodDescriptor(aMethod);
			}

		} catch (Throwable exception) {
			handleException(exception);
		}
		return aDescriptor;
	}
}
