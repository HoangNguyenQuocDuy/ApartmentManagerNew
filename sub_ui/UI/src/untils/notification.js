import { toast } from "react-toastify";

export const notify = (content, status) => {
    console.log('-----------TOAST-----------')
    if (status === 'success') {
        toast.success(content, {
            position: "top-right"
        });
    } else if (status === 'error') {
        console.log(12)

        toast.error(content, {
            position: "top-right"
        });
    } else if (status === 'warn') {
        toast.warn(content, {
            position: "top-right"
        });
    } else {
        toast.info(content, {
            position: "top-right"
        });
    }
}