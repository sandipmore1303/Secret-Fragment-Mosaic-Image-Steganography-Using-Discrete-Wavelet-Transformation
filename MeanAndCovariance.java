
import Jama.Matrix;

public class MeanAndCovariance {
       /**
042         * The mean vector (1xN)
043         */
       public final Matrix mean;

        /**
         * The covariance matrix (NxN)
048         */
        public final Matrix covar;

      /**
052         * Construct a new {@link MeanAndCovariance} containing the mean vector and
053         * covariance matrix of the given data (each row is a data point)
054         * 
055         * @param samples
056         *            the data
057         */
       public MeanAndCovariance(float[][] samples)
         {
                 final int nsamples = samples.length;
                  final int ndims = samples[0].length;
 
                 mean = new Matrix(1, ndims);
                 covar = new Matrix(ndims, ndims);
 
                 // mean
                 for (int j = 0; j < nsamples; j++) {
                         for (int i = 0; i < ndims; i++) {
                                 mean.set(0, i, mean.get(0, i) + samples[j][i]);
                         }
                 }
                 for (int i = 0; i < ndims; i++) {
                         mean.set(0, i, mean.get(0, i) / nsamples);                }

                // covar
                for (int i = 0; i < ndims; i++) {
                        for (int j = 0; j < ndims; j++) {
                                double qij = 0;

                                for (int k = 0; k < nsamples; k++) {
                                        qij += (samples[k][i] - mean.get(0, i)) * (samples[k][j] - mean.get(0, j));
                                }

                                covar.set(i, j, qij / (nsamples - 1));
                        }
                }
        }

        /**
         * Construct a new {@link MeanAndCovariance} containing the mean vector and
092         * covariance matrix of the given data (each row is a data point)
093         * 
094         * @param samples
095         *            the data
096         */
        public MeanAndCovariance(double[][] samples)
        {
                final int nsamples = samples.length;
                final int ndims = samples[0].length;

                mean = new Matrix(1, ndims);
                covar = new Matrix(ndims, ndims);

                // mean
                for (int j = 0; j < nsamples; j++) {
                        for (int i = 0; i < ndims; i++) {
                                mean.set(0, i, mean.get(0, i) + samples[j][i]);
                        }
                }
                for (int i = 0; i < ndims; i++) {
                        mean.set(0, i, mean.get(0, i) / nsamples);
                }

                // covar
                for (int i = 0; i < ndims; i++) {
                        for (int j = 0; j < ndims; j++) {
                                double qij = 0;

                                for (int k = 0; k < nsamples; k++) {
                                        qij += (samples[k][i] - mean.get(0, i)) * (samples[k][j] - mean.get(0, j));
                                }

                                covar.set(i, j, qij / (nsamples - 1));
                        }
                }
        }

        /**
         * Construct a new {@link MeanAndCovariance} containing the mean vector and
1         * covariance matrix of the given data (each row is a data point)
32         * 
133         * @param samples
134         *            the data
135         */
        public MeanAndCovariance(Matrix samples) {
                this(samples.getArray());
        }

        /**
         * Get the mean vector
142         * 
143         * @return the mean vector
144         */
        public Matrix getMean() {
                return mean;
        }

        /**
150         * Get the covariance matrix
151         * 
152         * @return the covariance matrix
153         */
        public Matrix getCovariance() {
                return covar;
        }

        /**
9         * Get the mean of the data
160         * 
161         * @param samples
162         *            the data
163         * @return the mean
164         */
        public static Matrix computeMean(float[][] samples) {
                return new MeanAndCovariance(samples).mean;
        }

        /**
170         * Get the covariance of the data
171         * 
172         * @param samples
173         *            the data
174         * @return the covariance matrix
175         */
        public static Matrix computeCovariance(float[][] samples) {
                return new MeanAndCovariance(samples).covar;
        }

       /**
181         * Get the mean of the data
182         * 
183         * @param samples
184         *            the data
185         * @return the mean
186         */
        public static Matrix computeMean(double[][] samples) {
                return new MeanAndCovariance(samples).mean;
        }

       /**
192         * Get the covariance of the data
193         * 
194         * @param samples
195         *            the data
196         * @return the covariance matrix
197         */
        public static Matrix computeCovariance(double[][] samples) {
               return new MeanAndCovariance(samples).covar;
        }

        /**
        * Get the mean of the data
204         * 
205         * @param samples
206         *            the data
207         * @return the mean
208         */
        public static Matrix computeMean(Matrix samples) {
               return new MeanAndCovariance(samples).mean;
        }
        /**
      * Get the covariance of the data
215         * 
216         * @param samples
217         *            the data
218         * @return the covariance matrix
219         */
        public static Matrix computeCovariance(Matrix samples) {
               return new MeanAndCovariance(samples).covar;
        }
}



























































