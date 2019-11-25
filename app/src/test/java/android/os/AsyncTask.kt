package android.os


import androidx.annotation.MainThread
import java.util.concurrent.Executor

/**
 * FOR THE SAKE OF THE TESTS!!
 */
abstract class AsyncTask<Params, Progress, Result> {

    protected abstract fun doInBackground(vararg params: Params): Result

    protected open fun onPostExecute(result: Result) {}

    @MainThread
    open fun execute(vararg params: Params): AsyncTask<Params, Progress, Result> {
        return executeOnExecutor(Executor { command -> command.run() }, *params)
    }

    @MainThread
    open fun executeOnExecutor(exec: Executor, vararg params: Params): AsyncTask<Params, Progress, Result> {
        exec.execute {
            val result = doInBackground(*params)
            onPostExecute(result)
        }

        return this
    }
}