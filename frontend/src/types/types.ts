import { Dayjs } from "dayjs";

export type Cost = {
    id: number;
    amount: number;
    date: string;
    date_unix: number;
    title: string;
    quantity: number;
    user_id: number;
    __typename?: string;
    refetchCosts?: Function;
    refetchCostsFilter?: Function;
    isFiltered: boolean;
};

export type CostFilterType = {
    title?: string | null;
    fromDate?: Dayjs | null;
    toDate?: Dayjs | null;
};
