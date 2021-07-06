import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaEnlazada<E> implements Iterable<E>{		
	private NodoLE<E> inicio, //unico atributo necesario para que funcione
					  fin;
	private int size; //sin este atributo, seria un problema de complejidad lineal, pues se tendria que recorrer todo el arreglo para saber el tamaño de la lista
	
	public ListaEnlazada(){
		//super(); esto seria la equivalencia porque el constructor default crea la lista vacia
		this.inicio = this.fin = null;
		this.size = 0;
	}
	
	public ListaEnlazada(E[] valores){
		for(int i=0;i<valores.length;i++) {
			insertarInicio(valores[valores.length-i-1]);
		}
		//incializa la lista con los elementos del arreglo
		//Ej: si recibo (6,4,10,3) inicio = 6,4,10,3
	}
	
	public E inicio() throws NoSuchElementException{ //te da el valor del inicio
		try {
			return this.inicio.valor; 
		}catch(NullPointerException ex) { //esto dice que la lista no se ha creado cuando realmente, la lista se creo pero con un null dentro de la lista
			throw new NoSuchElementException("No se puede regresar el primer elemento de una lista vacia"); //ponemos este tipo de error para marcar que no puede regresar null
		}
	}
	
	public E fin() throws NoSuchElementException{ //te da el valor del final
		try {
			return this.fin.valor; 
		}catch(NullPointerException ex) { 
			throw new NoSuchElementException("No se puede regresar el ultimo elemento de una lista vacia"); 
		}
	}
	
	public int size() { //puedes regresar nomas la operacion
		return this.size;
	}
	
	public boolean isEmpty() {
		return this.size==0;
	}
	
	public void insertarInicio(E valor) {
		/*NodoLE <E> nodo = new NodoLE<E>(valor);
		nodo.next = this.inicio;
		this.inicio = nodo;
		this.size++;*/
		this.inicio = new NodoLE<E>(valor,this.inicio);
		if(this.fin==null) {
			this.fin=this.inicio;
		}
		this.size++;
	}
	
	public void insertarFin(E valor) {
		if(this.size>0) {
			NodoLE<E> nodo = new NodoLE<E>(valor);
			this.fin.next = nodo;
			this.fin= this.fin.next;
			this.size++;
		}else {
			insertarInicio(valor);
		}
	}
	//insertarEn crece la lista
	//borrarEn decrece la lista
	
	public void insertarEn(E valor, int pos) {
		NodoLE<E> current = this.inicio;
		NodoLE<E> tmp = current;
		for(int i=0;i<pos;i++) {
			current = current.next;
		}
		
	}
	
	public void setAt(E valor, int pos) {//reemplaza el valor de la lista
		NodoLE<E> current = this.inicio;
		if(pos>size) {
			for(int i=0;i<pos;i++) {
				current = current.next;
			}
			current.setValor(valor);
		}else {
			throw new IndexOutOfBoundsException("No se puede cambiar el valor de un dato fuera de la lista");
		}
	}
	
	public E borrarInicio() {
		try {
			NodoLE<E> current = this.inicio;
			E valor = this.inicio();
			this.inicio = current.next;
			this.size--;
			return valor;
		}catch(NullPointerException ex) {
			throw new NoSuchElementException("No se puede eliminar el primer elemento de una lista vacia");
		}
		//Borra de la lista el primer elemento y me regresa el
		//valor que estaba ahi. Arroja un NoSuchElementException si 
		//la lista esta vacia
	}
	
	public E borrarFin() {
		try {
			E valor = this.fin();
			if(this.size>1) {
				NodoLE<E> current = this.inicio;
				for(int i=0;i<size-2;i++) {
					current=current.next;
				}
				current.next = null;
				this.fin=current;
				this.size--;
				return valor;
			}else {
				this.inicio=this.fin=null;
				this.size=0;
				return valor;
			}
			
		}catch(NullPointerException ex) {
			throw new NoSuchElementException("No se puede eliminar el ultimo elemento de una lista vacia");
		}
	}
	
	public E borrarEn(int pos) throws NoSuchElementException {
		if (pos == 0) {
			return borrarInicio();
		} else if (pos >= 0 && pos <= this.size) {
			NodoLE<E> current = this.inicio;
			for (int i = 1; i != pos; i++) {
				current = current.next;
			}
			NodoLE<E> aux = current.next;
			current.next = aux.next;
			E valor = aux.getValor();
			this.size--;
			return valor;
		} else if (pos == this.size - 1) {
			return borrarFin();
		} else {
			throw new NoSuchElementException("");
		}
	}
	
	public void borrarDuplicados() {
		HashSet<E> set = new HashSet<E>();
		NodoLE<E> nodo = this.inicio;
		NodoLE<E> prev = this.inicio;
		
		while(nodo != null) {
			if(set.contains(nodo.valor)) {
				prev.next = nodo.next;
				this.size--;
			}else {
				set.add((E) nodo.valor);
				prev = nodo;
			}
			nodo = nodo.next;
		}
	}
	
	public String toString() {
		String res = "";
		NodoLE<E> current=this.inicio;
		for(int i=0;i<this.size;i++) {
			res+=current.valor+",";
			current=current.next;
		}
		return res;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer[] valores = {1,4,3,2,5};
		ListaEnlazada<Integer> lista = new ListaEnlazada<Integer>(valores);
		Iterator<Integer> i1 = lista.iterator();
		while(i1.hasNext()) {
			System.out.println(i1.next());
		}	
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return new Iterator<E>() {
			NodoLE<E> current = ListaEnlazada.this.inicio;
			int pos = 0;
			boolean callRemove = false;
			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return pos<size;
			}

			@Override
			public E next() {
				// TODO Auto-generated method stub
				if(hasNext()) {
					callRemove = true;
					NodoLE<E> tmp = current;
					current = tmp.next;
					pos++;
					return tmp.valor;
				}else {
					throw new NoSuchElementException("No hay elemento a encontrar");
				}
			}
			
			public void remove() {
				if(callRemove) {
					borrarEn(pos);
				}
			}
		};
	}
}
//------------------------------------------------------------------------------------------//
class NodoLE<E>{
	E valor;
	NodoLE<E> next;
	 
	public NodoLE(E valor) {
		this(valor,null);
	}
	
	public NodoLE(E valor, NodoLE<E> next) {
		super();
		this.valor = valor;
		this.next = next;
	}
	
	public E getValor() {
		return valor;
	}
	
	public void setValor(E valor) {
		this.valor = valor;
	}
	
	public NodoLE<E> getNext() {
		return next;
	}
	
	public void setNext(NodoLE<E> next) {
		this.next = next;
	}
	
	public String toString() {
		return this.valor.toString(); //regresa el valor que representa
	}
	 
 }
