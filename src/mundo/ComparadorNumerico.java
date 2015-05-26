package mundo;

import java.util.Comparator;

public class ComparadorNumerico implements Comparator<Material>{

	@Override
	public int compare(Material o1, Material o2) {
		return Integer.compare(o1.getPedidos().size(), o2.getPedidos().size());
	}



}
