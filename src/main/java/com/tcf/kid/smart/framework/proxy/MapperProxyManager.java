package com.tcf.kid.smart.framework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tcf.kid.smart.framework.config.Configuration;
import com.tcf.kid.smart.framework.core.SqlSession;
import com.tcf.kid.smart.framework.model.Function;
import com.tcf.kid.smart.framework.model.MapperBean;

/***
 * TODO TCF ӳ�����ӿڴ�������
 * TODO TCF ����ע��KidORM ����������Ϣ��SQL�Ự
 * TODO TCF ��������������ڷ������SQL�Ự��SQLִ�в���ʵ�ֳ־û�����
 * @author 71485
 *
 */
public class MapperProxyManager implements InvocationHandler{

	//TODO TCF KidORM ����������Ϣ
	private Configuration configuration=null;
	
	//TODO TCF KidORM SQL�Ự
	private SqlSession sqlSession=null;
	
	//TODO TCF ����ע��
	public MapperProxyManager(Configuration configuration,SqlSession sqlSession)
	{
		this.configuration=configuration;
		this.sqlSession=sqlSession;
	}
	
	//TODO TCF �����������ڷ������SQL�Ự��SQLִ�в���ʵ�ֳ־û�����
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
	{
		Object invokeResult=null;
		
		try
		{
			//TODO TCF ����ӳ�����ӿ�ģ��
			MapperBean mapperBean=configuration.loadMapperFile("UserInfoMapper.xml");
			
			if(mapperBean!=null)
			{
				//TODO TCF ӳ�����ӿ���
				String interfaceName=mapperBean.getInterfaceName();
				
				//TODO TCF Ŀ�귽������ӳ�����ӿ�������͵�ǰ���ص�ӳ�����ӿ�����ͬ
				if(StringUtils.isNotEmpty(interfaceName) && interfaceName.equals(method.getDeclaringClass().getName()))
				{
					//TODO TCF ӳ�����ӿ��ж�������г־û�����
					List<Function> functions=mapperBean.getFunctions();
					
					if(functions!=null && functions.size()>0)
					{
						for(Function function:functions)
						{
							//TODO TCF ��Ҫ���õĳ־û�����������͵�ǰ���صķ�������ͬ
							if(StringUtils.isNotEmpty(function.getMethodName()) && function.getMethodName().equals(method.getName()))
							{
								invokeResult=sqlSession.selectOne(function.getSql(),args[0]!=null?args[0].toString():"");
							}
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return invokeResult;
	}
}
