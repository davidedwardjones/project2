Implementation:
Nathan and Adam dealt with learning the followers reaction function. 
First we implemented offline learning, using the ordinary least squares method to do linear regression. 

Isaac and David dealth with implementing the maximising function. Initially used brute force to calculate the maximum of the curve, but then figured out how to find the maximum using the equations from lectures. 

Nathan and Adam implemented windowing then implemented weighted least square with forgetting factor of 0.95. We then tested every combination of regression + windowing and found that the combination that generated the most profit (when summing the profits against the 3 models), was to use ordinary least squares method with a window size of 20.

The code in the submitted Leader.java calls doRegression1() which is the ordinary least squares with a window of 20. doRegression2() uses windowing and weighted least squares with forgetting factor but isn't called.
