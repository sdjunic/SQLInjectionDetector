import java.sql.SQLException;

public class SimpleWhileLoopTest4 {
	
	class AA
	{
		public String a1;
		public String a2;
		
		public boolean condition(String s)
		{
			this.a1 = this.a2;
			this.a2 = s;
		}
	};
	
	public static void main(String[] args) throws SQLException {
			
		AA aa = new AA();
		String a = null;
		String b = null;
				
		// Note:
		//	One logical loop iteration is (loop body + condiiton).
		//	Because of that, aa.a2 and a will be set in the same iteration.
		while(aa.condition(a))
		{
			a = b;
			b = "safe";
		}
		
		__check_4();
	}
}
