package wang.igood.db;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mysql.jdbc.StringUtils;

import wang.igood.db.core.PageInfo;

/***
 * 链接通道的初始化
 * */
public class DbUtils{

	private static DruidDataSource dds;
	
	public static synchronized void initDb(Properties properties) throws Exception {
		dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
	}
	
	public static Connection getCon() throws SQLException {
		return dds.getConnection();
	}

	public static boolean closeCon() {
		try {
			if(getCon() != null) {
				getCon().close();
			}
			return true;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**1:数据操操作封装******************************************************************************************************************************/
	/***
	 * <a>查询数据列表</a>
	 * @param T 对象实体
	 * @return List<T> 数据集合
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> queryList(T t){
		try {
			QueryRunner qr = new QueryRunner();
			String sql = "select "+SqlUtils.getColumn(t.getClass(),true) + " from "+SqlUtils.getTable(t.getClass(),true)+" "+SqlUtils.getWhere(t,true);
			System.out.println(sql);
			List<T> list= (List<T>) qr.query(getCon(),sql, new BeanListHandler(t.getClass()),SqlUtils.getParams(t));
			return list;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * <a>查询数据列表</a>
	 * @param T 对象实体
	 * @param pageInfo	分页对象
	 * @return List<T> 数据集合
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> queryList(T t,PageInfo pageInfo){
		try {
			QueryRunner qr = new QueryRunner();
			Integer currentIndex = pageInfo.getPageSize() * (pageInfo.getPageNum() - 1);
			String limit = " limit "+currentIndex + " , "+ currentIndex + pageInfo.getPageSize();
			String sql = "select "+SqlUtils.getColumn(t.getClass(),true) + " from "+SqlUtils.getTable(t.getClass(),true)+" "+SqlUtils.getWhere(t,true) + limit;
			List<T> list= (List<T>) qr.query(getCon(),sql, new BeanListHandler(t.getClass()),SqlUtils.getParams(t));
			return list;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/***
	 * <a>查询数据列表</a>
	 * @param String sql语句
	 * @return Class<T> 数据对象Class
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> List<T> queryList(String sql,Class<T> clazzs) {
		try {
			QueryRunner qr = new QueryRunner();
			List<T> list= (List<T>) qr.query(getCon(),sql, new BeanListHandler(clazzs));
			return list;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * <a>查询数据对象</a>
	 * @param T 查询对象
	 * @return T 结果对象
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T query(T t){
		try {
			QueryRunner qr = new QueryRunner();
			String sql = "select "+SqlUtils.getColumn(t.getClass(),true) + " from "+SqlUtils.getTable(t.getClass(),true)+" "+SqlUtils.getWhere(t,true);
			T data = (T) qr.query(getCon(),sql, new BeanHandler(t.getClass()),SqlUtils.getParams(t));
			return data;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * <a>查询数据对象</a>
	 * @param String sql语句
	 * @param Class<T> clazzs 查询对象Class
	 * @return T 结果对象
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T query(String sql,Class<T> clazzs) {
		try {
			QueryRunner qr = new QueryRunner();
			T t = (T) qr.query(getCon(),sql, new BeanHandler(clazzs));
			return t;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/***
	 * <a>插入数据对象</a>
	 * @param <T>
	 * @param T 数据对象
	 * @return T
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> BigInteger insert(T t) {
		 try {
			 QueryRunner qr = new QueryRunner();
			 String column = SqlUtils.getNotNullColumn(t,false);
			 StringBuffer values = new StringBuffer();
			 for(int i = 0,length = column.split(",").length;i<length;i++) {
				 if(!StringUtils.isNullOrEmpty(values.toString()))
					 values.append(" , ");
				 values.append(" ? ");
			 }
			 String sql = "insert into " + SqlUtils.getTable(t.getClass(),false) + " ("+column+") values("+values+")";
			 Object idObj = qr.insert(getCon(),sql, new ScalarHandler(1) , SqlUtils.getParams(t));
			 return BigInteger.valueOf(Long.parseLong(idObj.toString()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return null;
	}
	
	/***
	 * <a>删除数据对象</a>
	 * @param <T>
	 * @param <T>
	 * @param T 数据对象
	 * @return T
	 * */
	public static  <T> boolean deleted(T t) {
		 try {
			 QueryRunner qr = new QueryRunner();
			 String sql = "delete from "+SqlUtils.getTable(t.getClass(), false) +"  "+SqlUtils.getWhere(t,false);
			 int id = qr.update(getCon(),sql,SqlUtils.getParams(t));
			 return id == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return false;
	}
	
	/***
	 * <a>删除数据对象</a>
	 * @param <T>
	 * @param <T>
	 * @param T 数据对象
	 * @return T
	 * */
	public static  <T> boolean deleted(String sql) {
		 try {
			 QueryRunner qr = new QueryRunner();
			 int id = qr.update(getCon(),sql);
			 return id == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return false;
	}
	
	/***
	 * <a>删除数据对象</a>
	 * @param <T>
	 * @return boolean
	 * */
	public static  <T> boolean update(T t) {
		 try {
			 QueryRunner qr = new QueryRunner();
			 String columns = SqlUtils.getWhere(t, false).replaceAll("where 1=1  and", "").replace("id = ?  and", "").replaceAll("and", ",") ;
			 String sql = "update "+SqlUtils.getTable(t.getClass(), false) +" set " + columns  +" where id = "+SqlUtils.getId(t);
			 System.out.println(sql);
			 int result = qr.update(getCon(),sql,SqlUtils.getParamsWidthOutId(t));
			 return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return false;
	}
	
	/***
	 * <a>更新数据对象</a>
	 * @param T 数据对象
	 * @return boolean
	 * */
	public static  <T> boolean update(String sql,Object ... param) {
		 try {
			 QueryRunner qr = new QueryRunner();
			 int result = qr.update(getCon(),sql,param);
			 return result == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 return false;
	}
	
	
	
}