

public class ArrayOper{
	 
	//�����������е�˳��Ϊ����
	//����nΪҪ��������飬leftΪ�����е���ʼλ�ã�rightΪ�����еĽ���λ��
	public  void quickSort(int n[], int left, int right) {
	    int dp;
	    if (left < right) {
	        dp = partition(n, left, right);
	        quickSort(n, left, dp - 1);
	        quickSort(n, dp + 1, right);
	    }
	 }
	 //��������(forһά)
	 private int partition(int n[], int left, int right) {
		 int pivot = n[left];
		 while (left < right) {
			 while (left < right && n[right] >= pivot)
				 right--;
			 if (left < right)
				 n[left] = n[right];
			 while (left < right && n[left] <= pivot)
				 left++;
			 if (left < right)
				 n[right] = n[left];
			 }
		 n[left] = pivot;
		 return left;
	}
	 
	 //�����ʵ�ʴ�С(Ҫ�������������ֵ������)
	public int realSize(int[] array){
		 int low = 0;
		 if(array==null){
			 return -1;
		 }
	        int high = array.length - 1;
	        while(low <= high)
	        {
	            int middle = (low + high) / 2;
	            if(0== array[middle])
	            {
	                int j=middle;
	                while(array[j]==0){
	                	j--;
	                	if(j<0){
	                		return 0;
	                	}
	                }
	                return j+1;
	            }
	            if(array[middle]>0)
	            {
	                low = middle + 1;
	            }
	        } 
	     return array.length;
	 }
	
    //���ַ�(����󣬷�������λ��)
    public  int binary(int[] array, int value){
    	int right=array.length-1;
        int low = 0;
        int high = right;
        while(low <= high)
        {
            int middle = (low + high) / 2;
            if(value == array[middle])
            {
                return middle;
            }
            if(value > array[middle])
            {
                low = middle + 1;
            }
            if(value < array[middle])
            {
                high = middle - 1;
            }
        }
        return -1;
    }
    public static int removeElement(final int[] nums, final int val)
    {
	    int resultLen = 0;
	    for(int i = 0; i < nums.length; i++)
	    {
	    if (nums[i] != val)
		    {
		    nums[resultLen] = nums[i];
		    resultLen++;
		    }
	    }
	    return resultLen;
    }   
}
