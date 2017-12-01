package wang.igooo.db;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wang.igooo.db.core.Column;
import wang.igooo.db.core.Table;

/***
 * <a>Sql工具类</a>
 * @author sunliang
 * @email 1130437154@qq.com
 * @since 2017-11-25
 * **/
public class SqlUtils {
	
	/**
	 * <a>获取实体ID</a>
	 * @param <T>
	 * */
	public static <T> BigInteger getId(T t) {
		try {
			Field idField = t.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			String idStr =  idField.get(t)+"";
			return new BigInteger(idStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BigInteger("0");
	}
	
	/***
	 * <a>获取查询的字段</a>
	 * @param clazzs
	 * */
	public static String getColumn(Class<?> clazzs,boolean tagAble) {
		try {
			StringBuffer columns = new StringBuffer();
			Table table = clazzs.getAnnotation(Table.class);
			String tableName = table.value();
			Field[] fields = clazzs.getDeclaredFields();
			if(fields != null && fields.length > 0) {
				for(int i = 0,size = fields.length;i<size;i++) {
					Column column = fields[i].getAnnotation(Column.class);
					if(column != null) {
						if(tagAble)
							columns.append(tableName+"."+column.value()+" as ");
						columns.append( fields[i].getName() + " ");
						if(i<(size-1))
							columns.append(" , ");
					}
				}
			}
			return columns.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/***
	 * <a>获取查询的字段</a>
	 * @param <T>
	 * @param clazzs
	 * */
	public static <T> String getNotNullColumn(T t ,boolean tagAble) {
		try {
			StringBuffer columns = new StringBuffer();
			Table table = t.getClass().getAnnotation(Table.class);
			String tableName = table.value();
			Field[] fields = t.getClass().getDeclaredFields();
			if(fields != null && fields.length > 0) {
				for(int i = 0,size = fields.length;i<size;i++) {
					fields[i].setAccessible(true);
					Column column = fields[i].getAnnotation(Column.class);
					if(column != null && fields[i].get(t) != null) {
						if(tagAble) {
							columns.append(tableName+"."+column.value()+" as " + fields[i].getName() + " ");
						}else {
							columns.append(column.value());
						}
						if(i<(size-1))
							columns.append(" , ");
					}
				}
			}
			return columns.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * <a>获取查询的表名</a>
	 * @param clazzs
	 * */
	public static String getTable(Class<?> clazzs,boolean tagAble) {
		try {
			StringBuffer tableSb = new StringBuffer();
			Table table = clazzs.getAnnotation(Table.class);
			if(table != null)
				tableSb.append(" " + table.value());
			if(tagAble) {
				tableSb.append(" "+table.value());
			}
			return tableSb.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * <a>获取查询条件</a>
	 * @param clazzs
	 * */
	public static String getWhere(Object object,boolean tagAble) {
		try {
			StringBuffer whereSb = new StringBuffer("where 1=1 ");
			Class<?> clazzs = object.getClass();
			Table table = clazzs.getAnnotation(Table.class);
			String tableName = table.value();
			
			Field[] fields = clazzs.getDeclaredFields();
			if(fields != null && fields.length > 0) {
				for(int i = 0,size = fields.length;i<size;i++) {
					fields[i].setAccessible(true);
					Column column = fields[i].getAnnotation(Column.class);
					if(column != null && fields[i].get(object) instanceof Object) {
						if(fields[i].get(object) != null)
							if(tagAble){
								whereSb.append(" and " + tableName + "."+column.value()+"=? ");
							}else {
								whereSb.append(" and "+column.value()+"=? ");
							}
					}
				}
			}
			return whereSb.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * <a>获取查询条件</a>
	 * @param clazzs
	 * */
	public static Object[] getParams(Object object) {
		try {
			List<Object> objects = new ArrayList<>();
			Class<?> clazzs = object.getClass();
			Field[] fields = clazzs.getDeclaredFields();
			if(fields != null && fields.length > 0) {
				for(int i = 0,size = fields.length;i<size;i++) {
					fields[i].setAccessible(true);
					if(fields[i].get(object) != null && fields[i].get(object) instanceof Boolean) {
						objects.add(Boolean.parseBoolean(fields[i].get(object)+"")?1:0);
					}else  if(fields[i].get(object) != null && fields[i].get(object) instanceof Object) {
						objects.add(fields[i].get(object));
					}
				}
			}
			return objects.toArray();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/***
	 * <a>获取查询条件</a>
	 * @param clazzs
	 * */
	public static Object[] getParamsWidthOutId(Object object) {
		try {
			List<Object> objects = new ArrayList<>();
			Class<?> clazzs = object.getClass();
			Field[] fields = clazzs.getDeclaredFields();
			if(fields != null && fields.length > 0) {
				for(int i = 0,size = fields.length;i<size;i++) {
					fields[i].setAccessible(true);
					if(fields[i].get(object) != null && !"id".equals(fields[i].getName())) {
						if(fields[i].get(object) instanceof String) {
							objects.add("'"+fields[i].get(object)+"'");
						}else if(fields[i].get(object) instanceof Boolean) {
							objects.add(Boolean.parseBoolean(fields[i].get(object)+"")?1:0);
						}else if(fields[i].get(object) instanceof Object) {
							objects.add(fields[i].get(object));
						}
					}
				}
			}
			return objects.toArray();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * <a>获取更新内容</a>
	 * @param clazzs
	 * */
	public static String getValues(Object object) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			StringBuffer valueBuffer = new StringBuffer();
			Class<?> clazzs = object.getClass();
			Field[] fields = clazzs.getDeclaredFields();
			if(fields != null && fields.length > 0) {
				for(int i = 0,size = fields.length;i<size;i++) {
					fields[i].setAccessible(true);
					if(fields[i].get(object) != null && fields[i].get(object) instanceof Object) {
						if(fields[i].get(object) instanceof String) {
							valueBuffer.append("'"+fields[i].get(object)+"'");
						}else if(fields[i].get(object) instanceof Boolean) {
							valueBuffer.append(Boolean.parseBoolean(fields[i].get(object)+"")?1:0);
						}else if(fields[i].get(object) instanceof Date) {
							valueBuffer.append("'"+sdf.format(fields[i].get(object))+"'");
						}else {
							valueBuffer.append(fields[i].get(object));
						}
						if(i<(size-1))
							valueBuffer.append(" , ");
					}
				}
			}
			return valueBuffer.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
