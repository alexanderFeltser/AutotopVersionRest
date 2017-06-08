package dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import interfaces.IDao;

public class DbExecutorTestDrive {

	static IDao dao = null;

	public static void main(String[] args) {

		doTest();

	}

	@SuppressWarnings("unchecked")
	static void doTest() {

		FileInputStream fis;

		try {

			File fl = new File("Resources\\dbProp.xml");
			if (!fl.exists()) {
				System.out.println("File Resources\\dbProp.xml  does not exists");
				System.exit(0);
			}
			fis = new FileInputStream(fl);
			Properties dbProp = new Properties();
			dbProp.loadFromXML(fis);
			fis.close();

			dao = new DbExecutor(dbProp.getProperty("url"), dbProp.getProperty("driver"), dbProp.getProperty("user"),
					dbProp.getProperty("pass"), Integer.parseInt(dbProp.getProperty("batchProceededRows")));

			int countPage = 1;
			// List<Integer> colimnTypes = dao.getColumnTypes(fimp);
			// List<Integer> precisionSize = dao.getPrecisionSize(fimp);
			// int countRows = dao.geTotalRows(fimp);

			// Map m = dao.fetchRows("hfpsrvrinf", "", 10);
			Map<Integer, List<Object>> m = null;
			try {
				m = dao.getServerCurrentVersion("autotop_qa");
				dao.insertNewVersion("autotop_qa", "eng:20.09.00;db:12345;pb:1");
				System.out.println(dao.getVersionServerName());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println(m);
			Map<Integer, List<Object>> excel_data = null;

			while (true) {
				try {
					// excel_data = dao.fetchRows(fimp, countPage);
					if (excel_data.size() == 0)
						break;

					for (Map.Entry<Integer, List<Object>> entry : excel_data.entrySet()) {

						List<Object> listObjects = entry.getValue();

						for (Object object : listObjects) {
							System.out.print(entry.getKey() + " " + object.toString() + ",");
						}
					}

				} catch (Exception e) {
					break;
				}
				countPage++;
			}

		}

		catch (Exception e) {

			e.printStackTrace();
		} finally {
			// dao.endProcess();

		}
	}

}
